// Pomocná funkce: celočíselné dělení nahoru (a, b > 0)
// Celé dělení nahoru: ceil(a / b) pro kladné hodnoty
private fun ceilDiv(a: Long, b: Long): Long {
    require(b > 0) { "b must be positive" }
    // V AoC vstupu máme kladná čísla, takže tahle zjednodušená verze stačí
    return if (a >= 0L) (a + b - 1L) / b else a / b
}

fun main() {

    fun part1(input: List<String>): Long {
        val startTime = System.nanoTime()

        // Pomocná funkce 10^exp jako Long
        fun pow10(exp: Int): Long {
            var result = 1L
            repeat(exp) { result *= 10 }
            return result
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

        return sum
    }

    fun part2(input: List<String>): Long {
        // Stejně jako dřív: všechno je na jedné řádce, ale pro jistotu slepíme
        val line = input.joinToString(separator = "").trim()
        if (line.isEmpty()) return 0L

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
        if (ranges.isEmpty()) return 0L

        val minValue = ranges.minOf { it.first }
        val maxValue = ranges.maxOf { it.last }

        // Počet číslic maxValue – to nám ořízne maximální délky vzoru/opakování
        val maxDigits = maxValue.toString().length

        // Maximální délka vzoru k – minimálně 1, maximálně floor(maxDigits / 2)
        val maxK = maxDigits / 2
        if (maxK <= 0) return 0L

        // Předpočítáme 10^k pro k = 0..maxK
        val pow10 = LongArray(maxK + 1)
        pow10[0] = 1L
        for (i in 1..maxK) {
            pow10[i] = pow10[i - 1] * 10L
        }

        val seen = mutableSetOf<Long>()  // abychom nepočítali stejná čísla víckrát
        var sum = 0L

        for (k in 1..maxK) {
            val pow10k = pow10[k]
            val pMinGlobal = pow10k / 10L      // 10^(k-1)
            val pMaxGlobal = pow10k - 1L       // 10^k - 1

            // Kolikrát můžeme vzor opakovat, aby délka d = m*k nepřesáhla maxDigits
            val maxRepeats = maxDigits / k
            if (maxRepeats < 2) continue

            // Pro m = 2..maxRepeats
            for (m in 2..maxRepeats) {
                val digits = m * k

                // spočítáme factor = 1 + 10^k + 10^(2k) + ... + 10^((m-1)k)
                var factor = 0L
                var term = 1L
                repeat(m) { i ->
                    factor += term
                    if (i != m - 1) {
                        term *= pow10k
                    }
                }

                // Minimální a maximální možná hodnota n pro tento (k, m)
                // n_min = pMinGlobal * factor, n_max = pMaxGlobal * factor
                // (vždy v rámci Long, viz odůvodnění)
                val minN = pMinGlobal * factor
                if (minN > maxValue) {
                    // i nejmenší n je větší než všechno ve vstupu -> další m budou jen větší
                    break
                }
                val maxN = pMaxGlobal * factor
                if (maxN < minValue) {
                    // i největší n je menší než cokoli ve vstupu -> zkusíme větší m
                    continue
                }

                // Teď projdeme všechny vstupní intervaly
                for (range in ranges) {
                    val a = range.first
                    val b = range.last

                    // Rychlé vyhození intervalů, které se s [minN, maxN] nepotkají
                    if (b < minN || a > maxN) continue

                    // Chceme P tak, aby:
                    //   a <= P * factor <= b
                    // a zároveň P ∈ [pMinGlobal, pMaxGlobal]
                    var pLow = ceilDiv(a, factor)
                    var pHigh = b / factor

                    if (pLow < pMinGlobal) pLow = pMinGlobal
                    if (pHigh > pMaxGlobal) pHigh = pMaxGlobal
                    if (pLow > pHigh) continue

                    for (p in pLow..pHigh) {
                        val n = p * factor  // bezpečné: n <= b <= maxValue <= Long.MAX_VALUE
                        if (seen.add(n)) {
                            sum += n
                        }
                    }
                }
            }
        }

        return sum
    }

    // Test if implementation meets criteria from the description
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 1227775554L)
    check(part2(testInput) == 4174379265)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
