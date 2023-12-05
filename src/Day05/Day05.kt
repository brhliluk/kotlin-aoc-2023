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

    fun mapSeeds(seeds: List<Seed>, almanacs: List<Almanac>): MutableList<Seed> {
        val resultSeeds: MutableList<Seed> = mutableListOf()
        for (seed in seeds) {
            var updatedSeed = seed
            almanacs.forEach { almanac ->
                almanac.converters.firstOrNull { converter -> updatedSeed.id in converter.sourceRange }?.let {
                    updatedSeed = it.map(updatedSeed)
                }
            }
            println("Parsed seed $seed to $updatedSeed, seed #${seeds.indexOf(seed) + 1} from #${seeds.size}")
            resultSeeds.add(updatedSeed)
        }
        return resultSeeds
    }

    fun parseInput(input: List<String>, seedsAsRanges: Boolean = false): List<Seed>{
        var seeds: MutableList<Seed> = mutableListOf()
        val almanacs: MutableList<Almanac> = mutableListOf()
        input.forEach { line ->
            when {
                line.startsWith("seeds: ") -> {
                    val splitLine = line.substringAfter("seeds: ").split(' ')
                    if (!seedsAsRanges) seeds = splitLine.map { Seed(it.toLong()) }.toMutableList()
                    else {
                        var start = 0
                        splitLine.forEachIndexed { index, id ->
                            if (index % 2 == 0) start = id.toInt()
                            else seeds.addAll((start..(start + id.toInt())).map { Seed(it.toLong()) })
                        }
                    }
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
        if (seedsAsRanges) seeds = mapSeeds(seeds, almanacs)
        return seeds
    }

    fun part1(input: List<String>): Int {
        return parseInput(input).minOf { it.id }.toInt()
    }

    fun part2(input: List<String>): Int {
        return parseInput(input, seedsAsRanges = true).minOf { it.id }.toInt()
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
