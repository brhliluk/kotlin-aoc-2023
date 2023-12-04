package Day04

import println
import readInput
import kotlin.math.pow

data class Deck(
    val cards: List<Card>
) {
    val size get() = run {
        cards.forEachIndexed { index, card ->
            if (card.multiplier == 0) return@forEachIndexed
            (1..card.multiplier).forEach {
                cards[index + it].instances += card.instances
            }
        }
        cards.sumOf { it.instances }
    }
}

data class Card(
    val id: Int,
    val winningNumbers: MutableList<Int> = mutableListOf(),
    val ownNumbers: MutableList<Int> = mutableListOf(),
) {
    constructor(input: String): this(input.substringAfter("Card ").takeWhile { it != ':' }.trim().toInt()) {
        val numbers = input.substringAfter(": ").split(" | ").map { it.trim().split(' ') }
        winningNumbers.addAll(numbers[0].mapNotNull { if (it.isNotBlank()) it.trim().toInt() else null })
        ownNumbers.addAll(numbers[1].mapNotNull { if (it.isNotBlank()) it.trim().toInt() else null })
    }

    var instances = 1
    private val matchingNumbers: Set<Int> get() = winningNumbers.intersect(ownNumbers.toSet())
    val score: Int get() = 2.0.pow(matchingNumbers.size - 1).toInt()
    val multiplier get() = matchingNumbers.size
}

fun main() {

    fun part1(input: List<String>): Int {
        return input.sumOf { Card(it).score }
    }

    fun part2(input: List<String>): Int {
        return Deck(input.map { Card(it) }).size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("04", "Day04_test")
    val secondTestInput = readInput("04", "Day04_test")
    check(part1(testInput) == 13)
    check(part2(secondTestInput) == 30)

    val input = readInput("04", "Day04")
    part1(input).println()
    part2(input).println()
}
