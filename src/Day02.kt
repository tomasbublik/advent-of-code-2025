fun main() {
    fun part1(input: List<String>): Int {
        val startTime = System.nanoTime()
        // Součet všech "neplatných" ID v daných intervalech.
        // Neplatné ID: má sudý počet číslic 2k a je tvořeno zdvojením poloviny P: n = PP.
        // Ekvivalentně: n = P * (10^k + 1), kde P má právě k číslic (první číslice P != '0').

        fun pow10(exp: Int): Long {
            var res = 1L
            repeat(exp) { res *= 10L }
            return res
        }

        fun ceilDiv(a: Long, b: Long): Long = if (a >= 0) (a + b - 1) / b else a / b // vstupy očekáváme nezáporné
        fun floorDiv(a: Long, b: Long): Long = a / b

        fun sumConsecutive(l: Long, r: Long): Long {
            val n = r - l + 1
            // (l + r) * n / 2, dělejme v longu a vyhněme se přetečení přes dělení předem, pokud to jde
            return if (n % 2L == 0L) (l + r) * (n / 2) else ((l + r) / 2) * n
        }

        fun sumInvalidInRange(aIn: Long, bIn: Long): Long {
            if (aIn > bIn) return 0L
            var total = 0L
            // Pro k od 1 do 9 (max 18 číslic => 2k <= 18)
            for (k in 1..9) {
                val tenK = pow10(k)
                val tenK_1 = pow10(k - 1)
                val c = tenK + 1 // 10^k + 1

                // Rozsah P dle definice (k číslic, první číslice != 0)
                val pMin = tenK_1
                val pMax = tenK - 1

                // Odvozené omezení z intervalu [a,b]: P in [ceil(a/c), floor(b/c)]
                val pFromA = ceilDiv(aIn, c)
                val pFromB = floorDiv(bIn, c)

                val L = maxOf(pMin, pFromA)
                val R = minOf(pMax, pFromB)
                if (L <= R) {
                    val sumP = sumConsecutive(L, R)
                    total += c * sumP
                }
            }
            return total
        }

        // Parsování: můžeme mít vstup rozdělený do více řádků (např. zalomený příklad),
        // ale skutečný input bývá na jednom řádku. Vezmeme všechny řádky a splitneme podle ','.
        var answer = 0L
        for (line in input) {
            if (line.isBlank()) continue
            val parts = line.split(',')
            for (part in parts) {
                val token = part.trim()
                if (token.isEmpty()) continue
                val dash = token.indexOf('-')
                if (dash <= 0 || dash >= token.length - 1) continue // obrana proti špatnému tokenu
                val a = token.substring(0, dash).trim().toLong()
                val b = token.substring(dash + 1).trim().toLong()
                val lo = minOf(a, b)
                val hi = maxOf(a, b)
                answer += sumInvalidInRange(lo, hi)
            }
        }

        val result = answer.toInt()
        val elapsedMs = (System.nanoTime() - startTime) / 1_000_000
        println("[Day02][part1] elapsed ${elapsedMs} ms")
        return result
    }

    fun part1Faster(input: List<String>): Int {
        val startTime = System.nanoTime()

        // Pomocná funkce 10^exp jako Long
        fun pow10(exp: Int): Long {
            var result = 1L
            repeat(exp) { result *= 10 }
            return result
        }

        // Celé dělení nahoru: ceil(a / b) pro kladné hodnoty
        fun ceilDiv(a: Long, b: Long): Long {
            require(b > 0) { "b must be positive" }
            if (a >= 0) {
                return (a + b - 1) / b
            }
            // pro jistotu obecná varianta
            return a / b
        }

        // Vstup je ve skutečnosti jeden řádek, případně zalomený – sloupneme vše dohromady
        val line = input.joinToString(separator = "").trim()
        if (line.isEmpty()) return 0

        // Parsování intervalů "a-b" oddělených čárkami
        val ranges = mutableListOf<LongRange>()
        for (token in line.split(',')) {
            val t = token.trim()
            if (t.isEmpty()) continue
            val parts = t.split('-')
            require(parts.size == 2) { "Invalid range: $t" }
            val start = parts[0].toLong()
            val end = parts[1].toLong()
            ranges += start..end
        }
        if (ranges.isEmpty()) return 0

        // Najdeme globální maximum pro omezení délky čísel
        val maxValue = ranges.maxOf { it.last }
        val maxDigits = maxValue.toString().length

        // Budeme generovat jen čísla tvaru PP (dvě stejné poloviny) a deduplikovat je,
        // kdyby intervaly někde překrývaly.
        val seen = mutableSetOf<Long>()
        var sum = 0L

        for (k in 1..(maxDigits / 2)) {
            val pow10k = pow10(k)
            val base = pow10k + 1          // n = P * (10^k + 1)
            val pMinGlobal = pow10k / 10   // 10^(k-1) – bez vedoucí nuly
            val pMaxGlobal = pow10k - 1    // 10^k - 1

            val minN = pMinGlobal * base   // nejmenší možné PP pro dané k
            if (minN > maxValue) {
                // Pro větší k už budou všechna PP > maxValue, můžeme skončit
                break
            }
            val maxN = pMaxGlobal * base   // největší možné PP pro dané k

            for (range in ranges) {
                val a = range.first
                val b = range.last

                // Rychlý skip, pokud se interval vůbec nepotkává s [minN, maxN]
                if (b < minN || a > maxN) continue

                // Z nerovnosti a <= P*base <= b odvodíme P-rozsah:
                var pLow = ceilDiv(a, base)
                var pHigh = b / base

                // Omezíme na globální limity pro P (správná délka, bez leading zero)
                if (pLow < pMinGlobal) pLow = pMinGlobal
                if (pHigh > pMaxGlobal) pHigh = pMaxGlobal
                if (pLow > pHigh) continue

                for (p in pLow..pHigh) {
                    val n = p * base  // číslo typu PP
                    // Pro jistotu deduplikujeme (pokud jsou intervaly překryté)
                    if (seen.add(n)) {
                        sum += n
                    }
                }
            }
        }

        val elapsedMs = (System.nanoTime() - startTime) / 1_000_000
        println("[Day02][part1Faster] elapsed ${elapsedMs} ms")

        return sum.toInt()
    }

    fun part2(input: List<String>): Int {
        // TODO: Implement Day 02 - Part 2
        return 0
    }

    // Test if implementation meets criteria from the description
    val testInput = readInput("Day02_test")
     check(part1(testInput) == 1227775554)
     check(part1Faster(testInput) == 1227775554)
    // check(part2(testInput) == EXPECTED_PART2)

    val input = readInput("Day02")
    part1(input).println()
    part1Faster(input).println()
    part2(input).println()
}
