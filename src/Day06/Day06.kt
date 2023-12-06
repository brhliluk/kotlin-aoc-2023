package Day06

import println
import readInput

data class Race(
    val time: Long,
    val distance: Long,
) {
    val lowestWinningHoldTime: Long
        get() = (0..time).first { it * (time - it) > distance }

    val highestWinningHoldTime: Long
        get() = (time downTo 0).first { it * (time - it) > distance }
}

fun main() {

    fun part1(input: List<String>): Int {
        val times = input.first().substringAfter("Time:").trim().split(' ').filter { it.isNotBlank() }.map { it.toLong() }
        val distances = input[1].substringAfter("Distance:").trim().split(' ').filter { it.isNotBlank() }.map { it.toLong() }
        val races = mutableListOf<Race>().apply { for (i in times.indices) add(Race(times[i], distances[i])) }
        return races.map { race -> race.highestWinningHoldTime - race.lowestWinningHoldTime + 1 }.reduce { acc, i -> acc * i }.toInt()
    }

    fun part2(input: List<String>): Int {
        val time = input.first().substringAfter("Time:").trim().filter { it.isDigit()  }.toLong()
        val distance = input[1].substringAfter("Distance:").trim().filter { it.isDigit()  }.toLong()
        val race = Race(time, distance)
        return (race.highestWinningHoldTime - race.lowestWinningHoldTime + 1).toInt()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("06", "Day06_test")
    val secondTestInput = readInput("06", "Day06_test")
    check(part1(testInput) == 288)
    check(part2(secondTestInput) == 71503)

    val input = readInput("06", "Day06")
    part1(input).println()
    part2(input).println()
}
