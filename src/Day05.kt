fun main() {
    /**
     * Parses input into ranges and ingredient IDs.
     * Returns a pair of (ranges, ingredientIds).
     */
    fun parseInput(input: List<String>): Pair<List<LongRange>, List<Long>> {
        val splitIndex = input.indexOfFirst { it.isBlank() }
        require(splitIndex >= 0) { "Input must contain blank line separator" }

        val ranges = input.take(splitIndex)
            .map { line ->
                val (start, end) = line.split("-").map { it.toLong() }
                start..end
            }

        val ingredientIds = input.drop(splitIndex + 1)
            .filter { it.isNotBlank() }
            .map { it.toLong() }

        return ranges to ingredientIds
    }

    /**
     * Merges overlapping or adjacent ranges into non-overlapping ranges.
     * Returns sorted list of merged ranges.
     */
    fun mergeRanges(ranges: List<LongRange>): List<LongRange> {
        if (ranges.isEmpty()) return emptyList()

        val sorted = ranges.sortedBy { it.first }
        val merged = mutableListOf<LongRange>()
        var current = sorted.first()

        for (range in sorted.drop(1)) {
            // Check if ranges overlap or are adjacent (e.g., 10..14 and 15..20)
            if (range.first <= current.last + 1) {
                // Merge ranges
                current = current.first..maxOf(current.last, range.last)
            } else {
                // No overlap, save current and start new range
                merged.add(current)
                current = range
            }
        }
        merged.add(current)

        return merged
    }

    /**
     * Part 1: Counts how many ingredient IDs are fresh (fall within any range).
     */
    fun part1(input: List<String>): Long {
        val (ranges, ingredientIds) = parseInput(input)

        return ingredientIds.count { id ->
            ranges.any { range -> id in range }
        }.toLong()
    }

    /**
     * Part 2: Counts total number of unique IDs covered by all ranges.
     * Merges overlapping ranges first to avoid double-counting.
     */
    fun part2(input: List<String>): Long {
        val (ranges, _) = parseInput(input)
        val merged = mergeRanges(ranges)

        return merged.sumOf { range ->
            range.last - range.first + 1
        }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 3L)
    check(part2(testInput) == 14L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
