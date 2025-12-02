fun main() {
    val trackSize = 100
    val startPosition = 50

    fun parseInstruction(line: String): Pair<Char, Int> {
        val direction = line.first()
        val distance = line.drop(1).toInt()
        return direction to distance
    }

    fun move(position: Int, direction: Char, distance: Int, size: Int): Int = when (direction) {
        'L' -> (position - distance).mod(size)
        'R' -> (position + distance).mod(size)
        else -> position
    }

    fun stepOnce(position: Int, direction: Char, size: Int): Int =
        if (direction == 'R') (position + 1).mod(size) else (position - 1).mod(size)

    fun part1(input: List<String>): Int {
        var position = startPosition
        var hits = 0

        for (line in input) {
            val (direction, distance) = parseInstruction(line)
            position = move(position, direction, distance, trackSize)
            if (position == 0) hits++
        }

        return hits
    }

    fun part2(input: List<String>): Int {
        var position = startPosition
        var hits = 0

        for (line in input) {
            val (direction, steps) = parseInstruction(line)
            repeat(steps) {
                position = stepOnce(position, direction, trackSize)
                if (position == 0) hits++
            }
        }
        return hits
    }

    // Test if implementation meets criteria from the description
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 6)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
