private const val REQUIRED_DIGITS = 12

fun main() {
    /**
     * Part 1: Finds the maximum 2-digit number in each line by pairing digits optimally.
     * Uses a single right-to-left pass tracking the maximum digit seen so far.
     */
    fun part1(input: List<String>): Long {
        var total = 0L

        for (line in input.map { it.trim() }.filter { it.length >= 2 }) {
            var bestForLine = 0
            var suffixMaxDigit = 0

            for (i in line.lastIndex downTo 0) {
                val d = line[i] - '0'

                if (suffixMaxDigit > 0) {
                    bestForLine = maxOf(bestForLine, 10 * d + suffixMaxDigit)
                }

                suffixMaxDigit = maxOf(suffixMaxDigit, d)
            }

            total += bestForLine
        }

        return total
    }

    /**
     * Part 2: Finds the maximum REQUIRED_DIGITS-digit number from each line by removing excess digits.
     * Uses a monotonic stack algorithm to maintain the lexicographically largest sequence:
     * - Greedily removes smaller digits when a larger digit is encountered
     * - Ensures we keep exactly REQUIRED_DIGITS digits
     */
    fun part2(input: List<String>): Long =
        input.asSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .sumOf { line ->
                require(line.length >= REQUIRED_DIGITS) {
                    "Each bank must contain at least $REQUIRED_DIGITS digits, but found '${line.length}' in '$line'"
                }

                val n = line.length
                var toDrop = n - REQUIRED_DIGITS
                val stack = IntArray(n)
                var size = 0

                for (ch in line) {
                    val d = ch - '0'

                    // Remove smaller digits from stack while we can still drop digits
                    while (toDrop > 0 && size > 0 && stack[size - 1] < d) {
                        size--
                        toDrop--
                    }

                    stack[size++] = d
                }

                // Convert first REQUIRED_DIGITS digits to Long
                (0 until REQUIRED_DIGITS).fold(0L) { acc, i ->
                    acc * 10 + stack[i]
                }
            }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 357L)
    check(part2(testInput) == 3121910778619L)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
