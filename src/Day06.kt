// === Společné pomocné funkce ===

/** Vytvoří grid z řádků vstupu, doplní mezery na stejnou šířku */
private fun createGrid(input: List<String>): Array<CharArray> {
    val width = input.maxOf { it.length }
    return Array(input.size) { y ->
        CharArray(width) { x -> input[y].getOrElse(x) { ' ' } }
    }
}

/** Zjistí, zda je sloupec prázdný (obsahuje jen mezery) */
private fun isColumnEmpty(grid: Array<CharArray>, col: Int, rowRange: IntRange = grid.indices): Boolean =
    rowRange.all { grid[it][col] == ' ' }

/** Aplikuje operátor na seznam čísel */
private fun applyOperator(op: Char, numbers: List<Long>): Long = when (op) {
    '+' -> numbers.sum()
    '*' -> numbers.reduce { acc, n -> acc * n }
    else -> error("Neočekávaný operátor: $op")
}

/** Najde všechny operátory (+, *) v řádku */
private fun findOperators(row: CharArray): List<Char> =
    row.filter { it == '+' || it == '*' }

// === Part 1 pomocné funkce ===

private fun extractNumbersFromBlock(grid: Array<CharArray>, startCol: Int, endCol: Int): List<Long> {
    val numberRegex = Regex("""\d+""")
    return (0 until grid.size - 1).flatMap { y ->
        val rowSlice = String(grid[y].sliceArray(startCol..endCol))
        numberRegex.findAll(rowSlice).map { it.value.toLong() }
    }
}

private fun evaluateProblemBlock(grid: Array<CharArray>, startCol: Int, endCol: Int): Long {
    val lastRow = grid.last()
    val op = (startCol..endCol)
        .map { lastRow[it] }
        .first { it == '+' || it == '*' }

    val numbers = extractNumbersFromBlock(grid, startCol, endCol)
    return applyOperator(op, numbers)
}

private fun findBlockRanges(grid: Array<CharArray>): List<IntRange> {
    val width = grid[0].size
    val ranges = mutableListOf<IntRange>()
    var x = 0

    while (x < width) {
        // Přeskočíme prázdné sloupce
        while (x < width && isColumnEmpty(grid, x)) x++
        if (x >= width) break

        val start = x
        // Najdeme konec bloku
        while (x < width && !isColumnEmpty(grid, x)) x++
        ranges += start until x
    }
    return ranges
}

// === Part 2 pomocné funkce ===

private fun extractColumnNumber(grid: Array<CharArray>, col: Int, rowRange: IntRange): Long? {
    val digits = rowRange.map { grid[it][col] }.joinToString("").trim()
    return digits.takeIf { it.isNotEmpty() }?.toLong()
}

// === Hlavní funkce pro jednotlivé části ===

private fun part1(input: List<String>): Long {
    if (input.isEmpty()) return 0L

    val grid = createGrid(input)
    return findBlockRanges(grid).sumOf { range ->
        evaluateProblemBlock(grid, range.first, range.last)
    }
}

private fun part2(input: List<String>): Long {
    if (input.isEmpty()) return 0L

    val grid = createGrid(input)
    val digitsRows = 0 until grid.size - 1

    // Načteme operátory z posledního řádku (zleva doprava)
    val operations = findOperators(grid.last()).toMutableList()

    var total = 0L
    val numbers = mutableListOf<Long>()

    // Jdeme sloupce zprava doleva
    for (x in grid[0].size - 1 downTo 0) {
        val isBoundary = isColumnEmpty(grid, x, digitsRows)

        if (isBoundary) {
            if (numbers.isNotEmpty()) {
                require(operations.isNotEmpty()) {
                    "Ran out of operators for collected numbers $numbers"
                }
                val op = operations.removeLast()
                total += applyOperator(op, numbers)
                numbers.clear()
            }
        } else {
            // Z tohoto sloupce sestavíme číslo (nahoru -> dolů, bez operátorového řádku)
            val colStr = digitsRows.map { grid[it][x] }.joinToString("").trim()
            if (colStr.isNotEmpty()) {
                numbers.add(colStr.toLong())
            }
        }
    }

    // Zpracuj poslední batch čísel
    if (numbers.isNotEmpty() && operations.isNotEmpty()) {
        total += applyOperator(operations.removeLast(), numbers)
    }

    return total
}

// === Vstupní bod ===

fun main() {
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 4277556L)

    val testInput2 = readInput("Day06_test_2")
    check(part2(testInput2) == 3263827L)

    val input = readInput("Day06")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
