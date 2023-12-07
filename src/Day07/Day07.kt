package Day07

import println
import readInput

enum class Card(val id: Char) {
    TWO('2'), THREE('3'), FOUR('4'), FIVE('5'), SIX('6'), SEVEN('7'),
    EIGHT('8'), NINE('9'), T('T'), J('J'), Q('Q'), K('K'), A('A');

    companion object {
        fun byId(id: Char) = entries.firstOrNull { it.id == id } ?: error("No card with id: $id")
    }
}

data class Hand(
    val cards: List<Card>,
    val bid: Int,
    var type: Type = Type.HIGH_CARD
): Comparable<Hand> {
    init {
        val groupedCards = cards.groupBy { it }
        type = when {
            cards.all { it == cards.first() } -> Type.FIVE_OF_KIND
            groupedCards.keys.size == 2 -> {
                if (groupedCards.values.any { it.size == 4 }) Type.FOUR_OF_KIND
                else Type.FULL_HOUSE
            }
            groupedCards.keys.size == 3 -> {
                if (groupedCards.values.any { it.size == 3 }) Type.THREE_OF_KIND
                else Type.TWO_PAIR
            }
            groupedCards.keys.size == 4 -> Type.ONE_PAIR
            else -> Type.HIGH_CARD
        }
    }

    override operator fun compareTo(other: Hand): Int {
        if (this.type.strength != other.type.strength) return compareValues(this.type.strength, other.type.strength)
        else {
            cards.forEachIndexed { index, card ->
                if (card == other.cards[index]) return@forEachIndexed
                else return card.compareTo(other.cards[index])
            }
        }
        return 0
    }

    enum class Type(val strength: Int) {
        FIVE_OF_KIND(7), FOUR_OF_KIND(6), FULL_HOUSE(5),
        THREE_OF_KIND(4), TWO_PAIR(3), ONE_PAIR(2), HIGH_CARD(1)
    }
}

fun main() {

    fun parseHand(input: String) = Hand(input.take(5).map { Card.byId(it) }, input.split(' ')[1].toInt())

    fun part1(input: List<String>): Int {
        return input.map { parseHand(it) }.sortedBy { it }.mapIndexed { index, hand ->
            (index + 1) * hand.bid
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("07", "Day07_test")
    val secondTestInput = readInput("07", "Day07_test")
    check(part1(testInput) == 6440)
//    check(part2(secondTestInput) == 71503)

    val input = readInput("07", "Day07")
    part1(input).println()
    part2(input).println()
}
