private val DIRECTIONS = listOf(
    -1 to -1, -1 to 0, -1 to 1,
    0 to -1,           0 to 1,
    1 to -1,  1 to 0,  1 to 1
)

private const val ROBOT = '@'
private const val EMPTY = '.'
private const val MIN_NEIGHBORS_FOR_BLOCKED = 4

fun main() {
    /**
     * Counts neighbors of '@' at position (x, y) in the grid.
     */
    fun countNeighbors(grid: List<CharArray>, x: Int, y: Int): Int =
        DIRECTIONS.count { (dy, dx) ->
            val ny = y + dy
            val nx = x + dx
            ny in grid.indices && nx in grid[ny].indices && grid[ny][nx] == ROBOT
        }

    /**
     * Part 1: Counts accessible robots (those with fewer than 4 neighbors).
     */
    fun part1(input: List<String>): Long {
        if (input.isEmpty()) return 0L

        val grid = input.map { it.toCharArray() }

        return grid.indices.sumOf { y ->
            grid[y].indices.count { x ->
                grid[y][x] == ROBOT && countNeighbors(grid, x, y) < MIN_NEIGHBORS_FOR_BLOCKED
            }.toLong()
        }
    }

    /**
     * Part 2: Iteratively removes accessible robots until none remain.
     * In each iteration, all robots with fewer than 4 neighbors are removed simultaneously.
     */
    fun part2(input: List<String>): Long {
        if (input.isEmpty()) return 0L

        val grid = input.map { it.toCharArray() }.toTypedArray()
        var totalRemoved = 0L

        while (true) {
            val toRemove = buildList {
                for (y in grid.indices) {
                    for (x in grid[y].indices) {
                        if (grid[y][x] == ROBOT && countNeighbors(grid.toList(), x, y) < MIN_NEIGHBORS_FOR_BLOCKED) {
                            add(y to x)
                        }
                    }
                }
            }

            if (toRemove.isEmpty()) break

            toRemove.forEach { (y, x) -> grid[y][x] = EMPTY }
            totalRemoved += toRemove.size
        }

        return totalRemoved
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13L)
    check(part2(testInput) == 43L)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
