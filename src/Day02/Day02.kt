package Day02

import Day02.Game.Companion.toColor
import println
import readInput

data class Game(
    val id: Int,
    val cubeSets: MutableList<MutableList<Pair<Color, Int>>> = MutableList(100) { mutableListOf() }
) {
    fun isValid() = !cubeSets.any { set -> set.any { subSet -> subSet.second > subSet.first.maxCubes } }

    fun maxColor(color: Color): Int {
        var max = 0
        cubeSets.forEach { set ->
            val colorColection = set.filter { it.first == color }
            if (colorColection.isNotEmpty()) {
                colorColection.maxOf { it.second }.let {
                    if (it > max) max = it
                }
            }
        }
        return max
    }

    companion object {
        enum class Color(val colorName: String, val maxCubes: Int) {
            RED("red", 12),
            GREEN("green", 13),
            BLUE("blue", 14);
        }

        fun String.toColor(): Color = when (this) {
            Color.RED.colorName -> Color.RED
            Color.GREEN.colorName -> Color.GREEN
            Color.BLUE.colorName -> Color.BLUE
            else -> throw Error("Wrong color!")
        }
    }
}

fun main() {

    fun parseGame(input: String): Game {
        val game = Game(input.substringAfter("Game ").takeWhile { it != ':' }.toInt())
        input.substringAfter(":").split(';').forEachIndexed { index, set ->
            set.split(",").forEach { pair ->
                val splitPair = pair.stripLeading().split(" ")
                game.cubeSets[index].add(splitPair[1].toColor() to splitPair.first().toInt())
            }
        }
        return game
    }

    fun part1(input: List<String>): Int {
        return input.map { parseGame(it) }.filter { it.isValid() }.sumOf { it.id }
    }

    fun part2(input: List<String>): Int {
        return input.map { parseGame(it) }.map {
            it.maxColor(Game.Companion.Color.BLUE) * it.maxColor(Game.Companion.Color.GREEN) * it.maxColor(Game.Companion.Color.RED)
        }.sumOf { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("02", "Day02_test")
    val secondTestInput = readInput("02", "Day02_test")
    check(part1(testInput) == 8)
    check(part2(secondTestInput) == 2286)

    val input = readInput("02", "Day02")
    part1(input).println()
    part2(input).println()
}
