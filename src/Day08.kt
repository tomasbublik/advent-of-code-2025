import kotlin.math.sqrt

data class Point3D(val x: Int, val y: Int, val z: Int)

class UnionFind(size: Int) {
    private val parent = IntArray(size) { it }
    private val rank = IntArray(size) { 0 }

    fun find(x: Int): Int {
        if (parent[x] != x) {
            parent[x] = find(parent[x]) // Path compression
        }
        return parent[x]
    }

    fun union(x: Int, y: Int): Boolean {
        val rootX = find(x)
        val rootY = find(y)

        if (rootX == rootY) return false

        // Union by rank
        when {
            rank[rootX] < rank[rootY] -> parent[rootX] = rootY
            rank[rootX] > rank[rootY] -> parent[rootY] = rootX
            else -> {
                parent[rootY] = rootX
                rank[rootX]++
            }
        }
        return true
    }

    fun getCircuitSizes(): List<Int> {
        val circuits = mutableMapOf<Int, Int>()
        for (i in parent.indices) {
            val root = find(i)
            circuits[root] = circuits.getOrDefault(root, 0) + 1
        }
        return circuits.values.toList()
    }
}

fun main() {
    fun parsePoints(input: List<String>): List<Point3D> {
        return input.map { line ->
            val (x, y, z) = line.split(",").map { it.toInt() }
            Point3D(x, y, z)
        }
    }

    fun distance(p1: Point3D, p2: Point3D): Double {
        val dx = (p1.x - p2.x).toLong()
        val dy = (p1.y - p2.y).toLong()
        val dz = (p1.z - p2.z).toLong()
        return sqrt((dx * dx + dy * dy + dz * dz).toDouble())
    }

    fun part1(input: List<String>, connections: Int = 1000): Int {
        val points = parsePoints(input)
        val n = points.size

        // Vytvoříme všechny páry s jejich vzdálenostmi
        val pairs = mutableListOf<Triple<Int, Int, Double>>()
        for (i in 0 until n) {
            for (j in i + 1 until n) {
                val dist = distance(points[i], points[j])
                pairs.add(Triple(i, j, dist))
            }
        }

        // Seřadíme podle vzdálenosti
        pairs.sortBy { it.third }

        // Union-Find pro sledování obvodů
        val uf = UnionFind(n)

        // Připojíme prvních 'connections' nejbližších párů
        var connected = 0
        for ((i, j, _) in pairs) {
            if (connected >= connections) break
            uf.union(i, j)
            connected++
        }

        // Získáme velikosti obvodů
        val circuitSizes = uf.getCircuitSizes().sortedDescending()

        // Vynásobíme tři největší
        return circuitSizes.take(3).fold(1) { acc, size -> acc * size }
    }

    fun part2(input: List<String>): Long {
        val points = parsePoints(input)
        val n = points.size

        if (n <= 1) return 0L

        // Vytvoříme všechny páry s jejich vzdálenostmi
        val pairs = mutableListOf<Triple<Int, Int, Double>>()
        for (i in 0 until n) {
            for (j in i + 1 until n) {
                val dist = distance(points[i], points[j])
                pairs.add(Triple(i, j, dist))
            }
        }

        // Seřadíme podle vzdálenosti
        pairs.sortBy { it.third }

        // Union-Find pro sledování obvodů
        val uf = UnionFind(n)

        // Sledujeme počet oddělených komponent
        var components = n
        var lastI = -1
        var lastJ = -1

        // Spojujeme páry dokud nejsou všechny v jednom obvodu
        for ((i, j, _) in pairs) {
            if (components == 1) break

            // Union vrátí true, pokud byly body v různých obvodech
            if (uf.union(i, j)) {
                components--
                lastI = i
                lastJ = j
            }
        }

        // Vrátíme součin X souřadnic posledních dvou spojených bodů
        return points[lastI].x.toLong() * points[lastJ].x.toLong()
    }

    // Test s příkladovými daty
    val testInput = readInput("Day08_test")
    val testPart1 = part1(testInput, 10)
    println("Test Part 1: $testPart1 (expected 40)")
    check(testPart1 == 40) { "Test Part 1 failed: expected 40, got $testPart1" }

    val testPart2 = part2(testInput)
    println("Test Part 2: $testPart2 (expected 25272)")
    check(testPart2 == 25272L) { "Test Part 2 failed: expected 25272, got $testPart2" }

    // Reálná data
    val input = readInput("Day08")
    if (input.isNotEmpty() && input[0].isNotBlank()) {
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    } else {
        println("Day08.txt je prázdný - doplňte vstupní data z https://adventofcode.com/2025/day/8/input")
    }
}

