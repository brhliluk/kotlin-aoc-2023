package Day05

import println
import readInput
import kotlin.math.pow


fun main() {

    fun part1(input: List<String>): Int {
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("05", "Day05_test")
//    val secondTestInput = readInput("05", "Day05_test")
    check(part1(testInput) == 13)
//    check(part2(secondTestInput) == 30)

    val input = readInput("05", "Day05")
    part1(input).println()
    part2(input).println()
}
