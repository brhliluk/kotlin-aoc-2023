fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            "${line.first { it.isDigit() }}${line.last { it.isDigit() }}".toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val validNumbers = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

        fun getFirstCalibrationDigit(first: Pair<Int, *>?, second: Pair<Int, *>?): Pair<Int, *> {
            if (first == null) return second as Pair<Int, *>
            if (second == null) return first
            return if (first.first < second.first) first else second
        }

        fun getLastCalibrationDigit(first: Pair<Int, *>?, second: Pair<Int, *>?): Pair<Int, *> {
            if (first == null) return second as Pair<Int, *>
            if (second == null) return first
            return if (first.first > second.first) first else second
        }

        return input.sumOf { line ->
            val firstDigit = line.firstOrNull { it.isDigit() }
            val lastDigit = line.lastOrNull { it.isDigit() }
            val firstDigitIndexed = firstDigit?.let { digit -> line.indexOfFirst { it == digit } to digit }
            val lastDigitIndexed = lastDigit?.let { digit -> line.indexOfLast { it == digit } to digit }

            val firstNumber = line.findAnyOf(validNumbers)
            val lastNumber = line.findLastAnyOf(validNumbers)
            val firstNumberIndexed = firstNumber?.let { number -> number.first to validNumbers.indexOfFirst { it == number.second } + 1}
            val lastNumberIndexed = lastNumber?.let { number -> number.first to validNumbers.indexOfFirst { it == number.second } + 1}

            val firstCalibrationDigit = getFirstCalibrationDigit(firstDigitIndexed, firstNumberIndexed)
            val lastCalibrationDigit = getLastCalibrationDigit(lastDigitIndexed, lastNumberIndexed)

            "${firstCalibrationDigit.second}${lastCalibrationDigit.second}".toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    val secondTestInput = readInput("Day01_p2_test")
    check(part1(testInput) == 142)
    check(part2(secondTestInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
