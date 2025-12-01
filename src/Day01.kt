fun main() {
    fun part1(input: List<String>): Int {
        var position = 50
        var count = 0

        for (line in input) {
            val direction = line[0]
            val distance = line.substring(1).toInt()

            position = when (direction) {
                'L' -> (position - distance).mod(100)
                'R' -> (position + distance).mod(100)
                else -> position
            }

            if (position == 0) {
                count++
            }
        }

        return count
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // Test if implementation meets criteria from the description
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 3)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
