package Day03

import println
import readInput
import kotlin.math.abs

data class Coordinate(
    val x: Int,
    val y: Int,
) {
    fun isAdjacent(second: Coordinate): Boolean = when {
        (second.y == this.y && abs(second.x - this.x) == 1) -> true
        (second.x == this.x && abs(second.y - this.y) == 1) -> true
        (abs(second.x - this.x) == 1 && abs(second.y - this.y) == 1) -> true
        else -> false
    }

    fun isAdjacent(coordinates: List<Coordinate>): Boolean = coordinates.any { it.isAdjacent(this) }
}

data class Symbol(
    val identifier: Char,
    val coordinate: Coordinate
)

data class EnginePart(
    val id: Int,
    val line: Int,
    val startIndex: Int,
    val endIndex: Int,
) {
    val coordinates: List<Coordinate> = (startIndex..endIndex).map { Coordinate(it, line) }
}

data class Engine(
    val parts: MutableList<EnginePart> = mutableListOf(),
    val symbols: MutableList<Symbol> = mutableListOf()
) {
    val relevantParts get() = parts.filter { enginePart -> enginePart.coordinates.any {
        coordinate -> coordinate.isAdjacent(symbols.map { it.coordinate })
    } }
}

fun main() {

    fun parseSchematic(input: List<String>): Engine {
        val engine = Engine()
        input.forEachIndexed { y, schematicLine ->
            var startIndex = 0
            var number = ""
            fun resetNumber(endIndex: Int) {
                engine.parts.add(EnginePart(number.toInt(), y, startIndex, endIndex))
                startIndex = 0
                number = ""
            }
            schematicLine.forEachIndexed { x, character ->
                if (character.isDigit()) {
                    if (number.isBlank()) startIndex = x
                    number += character
                } else {
                    if (number.isNotBlank()) resetNumber(x - 1)
                    if (character != '.') engine.symbols.add(Symbol(character, Coordinate(x, y)))
                }
            }
            if (number.isNotBlank()) resetNumber(schematicLine.length - 1)
        }
        return engine
    }

    fun part1(input: List<String>): Int {
        val engine = parseSchematic(input)
        return engine.relevantParts.sumOf { it.id }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("03", "Day03_test")
//    val secondTestInput = readInput("03", "Day03_test")
    check(part1(testInput) == 4361)
//    check(part2(secondTestInput) == 2286)

    val input = readInput("03", "Day03")
    part1(input).println()
    part2(input).println()
}
