package Day05

import println
import readInput

@JvmInline
value class Seed(
    val id: Long
)

class ConverterRange(
    private val destinationStart: Long,
    private val sourceStart: Long,
    private val length: Long
) {
    constructor(line: String) : this(
        line.split(' ')[0].toLong(),
        line.split(' ')[1].toLong(),
        line.split(' ')[2].toLong(),
    )

    val sourceRange get() = sourceStart..(sourceStart + length)

    fun map(seed: Seed): Seed {
        if (seed.id !in sourceRange) return seed
        return Seed(destinationStart + sourceRange.indexOf(seed.id))
    }
}

data class Almanac(
    val from: String,
    val to: String,
    val seeds: List<Seed> = listOf(),
    val converters: MutableList<ConverterRange> = mutableListOf(),
) {
    fun updateSeeds(): MutableList<Seed> {
        val sourceSeeds: MutableList<Seed> = seeds.toMutableList()
        val resultSeeds: MutableList<Seed> = mutableListOf()
        for (converter in converters) {
            sourceSeeds.filter { seed -> seed.id in converter.sourceRange }.forEach { seed ->
                sourceSeeds.remove(seed)
                resultSeeds.add(converter.map(seed))
            }
        }
        resultSeeds.addAll(sourceSeeds)
        return resultSeeds
    }

}

fun main() {

    fun smallestSeed(seeds: List<String>, almanacs: List<Almanac>): Seed {
        var lowestSeed = Seed(Long.MAX_VALUE)
        val seedsRanges = seeds.mapIndexedNotNull { index, id ->
            if (index % 2 == 0) (id.toLong()..(id.toLong() + seeds[index + 1].toLong()))
            else null
        }
        for (range in seedsRanges) {
            for (seed in range) {
                var updatedSeed = Seed(seed)
                almanacs.forEach { almanac ->
                    almanac.converters.firstOrNull { converter -> updatedSeed.id in converter.sourceRange }?.let {
                        updatedSeed = it.map(updatedSeed)
                    }
                }
                if (updatedSeed.id < lowestSeed.id) lowestSeed = updatedSeed
            }
            println("Parsed range #${seedsRanges.indexOf(range) + 1} from #${seedsRanges.size}")
        }
        return lowestSeed
    }

    fun parseInput(input: List<String>, seedsAsRanges: Boolean = false): List<Seed>{
        var seeds: MutableList<Seed> = mutableListOf()
        val almanacs: MutableList<Almanac> = mutableListOf()
        var splitLine: List<String> = listOf()
        input.forEach { line ->
            when {
                line.startsWith("seeds: ") -> {
                    splitLine = line.substringAfter("seeds: ").split(' ')
                    if (!seedsAsRanges) seeds = splitLine.map { Seed(it.toLong()) }.toMutableList()
                }
                line.contains('-') -> {
                    // Before adding new almanac, get updated seeds for the last one to get correct input
                    if (almanacs.isNotEmpty()) println("Parsed almanac from ${almanacs.last().from} to ${almanacs.last().to}, seeds = $seeds")
                    if (almanacs.isNotEmpty() && !seedsAsRanges) seeds = almanacs.last().updateSeeds()
                    val mappingName = line.substringBefore(" map:").split('-')
                    almanacs.add(Almanac(mappingName[0], mappingName[2], if (!seedsAsRanges) seeds else listOf()))
                }
                line.isBlank() -> return@forEach
                else -> almanacs.last().converters.add(ConverterRange(line))
            }
        }
        if (!seedsAsRanges) seeds = almanacs.last().updateSeeds()
        println("Parsed almanac from ${almanacs.last().from} to ${almanacs.last().to}, seeds = $seeds")
        if (seedsAsRanges) seeds = mutableListOf(smallestSeed(splitLine, almanacs))
        return seeds
    }

    fun part1(input: List<String>): Int {
        return parseInput(input).minOf { it.id }.toInt()
    }

    fun part2(input: List<String>): Int {
        return parseInput(input, seedsAsRanges = true).first().id.toInt()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("05", "Day05_test")
    val secondTestInput = readInput("05", "Day05_test")
    check(part1(testInput) == 35)
    check(part2(secondTestInput) == 46)

    val input = readInput("05", "Day05")
//    part1(input).println()
    part2(input).println()
}
