data class Beam(val row: Int, val col: Int)

fun main() {
    fun parseGrid(input: List<String>): Array<CharArray> {
        return input.map { it.toCharArray() }.toTypedArray()
    }

    fun findStart(grid: Array<CharArray>): Beam? {
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                if (grid[row][col] == 'S') {
                    return Beam(row, col)
                }
            }
        }
        return null
    }

    fun countSplits(grid: Array<CharArray>, startBeam: Beam): Int {
        val activeBeams = mutableListOf(startBeam)
        val processed = mutableSetOf<Beam>()
        val hitSplitters = mutableSetOf<Pair<Int, Int>>()

        while (activeBeams.isNotEmpty()) {
            val beam = activeBeams.removeAt(0)

            if (!processed.add(beam)) continue

            var currentRow = beam.row + 1

            while (currentRow < grid.size) {
                val cell = grid[currentRow][beam.col]

                if (cell == '^') {
                    hitSplitters.add(currentRow to beam.col)

                    if (beam.col > 0) {
                        activeBeams.add(Beam(currentRow, beam.col - 1))
                    }
                    if (beam.col < grid[currentRow].size - 1) {
                        activeBeams.add(Beam(currentRow, beam.col + 1))
                    }

                    break
                }

                currentRow++
            }
        }

        return hitSplitters.size
    }

    fun part1(input: List<String>): Int {
        val grid = parseGrid(input)
        val start = findStart(grid) ?: return 0
        return countSplits(grid, start)
    }

    fun part2(input: List<String>): Int {
        // Part 2 bude přidán později
        return 0
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 21)

    val input = readInput("Day07")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
