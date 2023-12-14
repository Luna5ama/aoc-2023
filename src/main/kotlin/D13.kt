import java.io.File

fun main() {
    val input = File("inputs/D13.txt").readLines()

    data class Pattern(val id: Int, val lines: List<String>, val colBits: List<Int>, val rowBits: List<Int>)

    val patterns = buildList {
        var lines = mutableListOf<String>()
        fun makePattern(): Pattern {
            val rowBits = lines.map { it.replace('.', '0').replace('#', '1').toInt(2) }
            val colBits = (0..<lines[0].length).map { col ->
                lines.map { it[col] }.joinToString("").replace('.', '0').replace('#', '1').toInt(2)
            }
            return Pattern(this.size, lines, colBits, rowBits)
        }
        for (line in input) {
            if (line.isEmpty()) {
                add(makePattern())
                lines = mutableListOf()
            } else {
                lines.add(line)
            }
        }
        if (lines.isNotEmpty()) {
            add(makePattern())
        }
    }

    fun findMirror(bits: List<Int>, ex: Int): Int {
        val left = ArrayDeque<Int>()
        val right = ArrayDeque(bits)

        left.addFirst(right.removeFirst())

        while (right.size > 0) {
            var found = true
            for (i in 0..<minOf(left.size, right.size)) {
                if (right[i] != left[i]) {
                    found = false
                    break
                }
            }
            if (found && left.size != ex) {
                return left.size
            }

            left.addFirst(right.removeFirst())
        }

        return -1
    }

    val part1 = patterns.withIndex().sumOf { (i, it) ->
        val colMirror = findMirror(it.colBits, -1)
        val rowMirror = findMirror(it.rowBits, -1)

        if (colMirror != -1) {
            colMirror
        } else if (rowMirror != -1) {
            rowMirror * 100
        } else {
            error("No mirror found for pattern $i")
//            0
        }
    }
    println("Part 1: $part1")

    val part2 = patterns.map mapToInt@{ pattern ->
        var colMirrorOg = findMirror(pattern.colBits, -1)
        var rowMirrorOg = findMirror(pattern.rowBits, -1)

        if (colMirrorOg == -1) {
            colMirrorOg = -2
        }
        if (rowMirrorOg == -1) {
            rowMirrorOg = -2
        }

        val copy = pattern.lines.map { it.toCharArray() }
        val rowSize = pattern.lines[0].length

        fun set(x: Int, y: Int, c: Char) {
            copy[y][x] = c
        }

        fun set(i: Int, c: Char) {
            set(i % rowSize, i / rowSize, c)
        }

        fun flip(i: Int) {
            val c = copy[i / rowSize][i % rowSize]
            set(i, if (c == '#') '.' else '#')
        }

        val totalSize = pattern.lines[0].length * pattern.lines.size
        for (i in 0..<totalSize) {
            if (i > 0) {
                flip(i - 1)
            }
            flip(i)

            val rowBits = copy.map { it.concatToString().replace('.', '0').replace('#', '1').toInt(2) }
            val colBits = (0..<copy[0].size).map { col ->
                copy.map { it[col] }.joinToString("").replace('.', '0').replace('#', '1').toInt(2)
            }

            val newCol = findMirror(colBits, colMirrorOg)
            val newRow = findMirror(rowBits, rowMirrorOg)

            if (newCol != -1) {
                return@mapToInt newCol
            } else if (newRow != -1) {
                return@mapToInt newRow * 100
            }
        }

        error("No mirror found for pattern ${pattern.id}")
    }.sum()

    println("Part 2: $part2")
}