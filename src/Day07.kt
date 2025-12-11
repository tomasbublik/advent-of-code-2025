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

    fun part2(input: List<String>): Long {
        // Najdeme startovní pozici 'S'
        var startX = 0
        var startY = 0
        input.forEachIndexed { y, row ->
            val x = row.indexOf('S')
            if (x != -1) {
                startX = x
                startY = y
            }
        }

        // Mapa: X souřadnice -> Počet paprsků (cest), které tam dopadly
        // Používáme Map, protože paprsky se mohou dostat i mimo původní šířku řádku (do záporných čísel nebo doprava)
        var currentBeams = mutableMapOf<Int, Long>()

        // Na začátku máme 1 paprsek na startovní pozici
        currentBeams[startX] = 1L

        // Procházíme řádky od startu až dolů
        // Simulujeme, co se stane s paprsky v aktuálním řádku 'y', když padají do 'y+1'
        for (y in startY until input.size) {
            val nextBeams = mutableMapOf<Int, Long>()
            val currentRowString = input[y]

            for ((x, count) in currentBeams) {
                // Zjistíme, co je na aktuální pozici v mřížce.
                // Pokud jsme mimo hranice textu, předpokládáme prázdný prostor '.'
                val char = if (x in currentRowString.indices) currentRowString[x] else '.'

                if (char == '^') {
                    // Splitter: Paprsek se rozdělí vlevo (x-1) a vpravo (x+1) do dalšího řádku.
                    // Počet cest se přičte k oběma směrům.
                    nextBeams[x - 1] = nextBeams.getOrDefault(x - 1, 0L) + count
                    nextBeams[x + 1] = nextBeams.getOrDefault(x + 1, 0L) + count
                } else {
                    // Prázdný prostor '.' (nebo Start 'S'): Paprsek padá rovně dolů (x).
                    nextBeams[x] = nextBeams.getOrDefault(x, 0L) + count
                }
            }
            // Posuneme se o řádek níže
            currentBeams = nextBeams
        }

        // Výsledek je součet všech paprsků, které "vypadly" z posledního řádku
        return currentBeams.values.sum()
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 40L)

    val input = readInput("Day07")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
