import kotlin.io.path.Path
import kotlin.io.path.readText

// Test funkce
fun testDay01() {
    println("=== Test Day 01 ===")

    // Testovací data
    val testInput = Path("src/Day01_test.txt").readText().trim().lines()

    var position = 50
    var count = 0

    println("Začátek na pozici: $position")

    for (line in testInput) {
        val direction = line[0]
        val distance = line.substring(1).toInt()

        val oldPosition = position
        position = when (direction) {
            'L' -> (position - distance).mod(100)
            'R' -> (position + distance).mod(100)
            else -> position
        }

        println("$line: $oldPosition -> $position" + if (position == 0) " ✓ COUNT" else "")

        if (position == 0) {
            count++
        }
    }

    println("\nVýsledek: $count")
    println("Očekáváno: 3")
    println(if (count == 3) "✓ TEST PROŠEL" else "✗ TEST SELHAL")
}

fun main() {
    testDay01()
}

