import java.io.File
import kotlin.math.abs
import kotlin.streams.asStream

fun main() {
    val input = File("inputs/D11.txt").readText()
    val original = input.lines()

    data class Point(val x: Int, val y: Int)

    val expandedColumns = original[0].indices.filter { x ->
        original.none {
            it[x] == '#'
        }
    }.toSet()

    val expandedRows = original.indices.filter { y ->
        original[y].none {
            it == '#'
        }
    }.toSet()

    val stars = original.indices.flatMap { y ->
        original[y].indices.mapNotNull { x ->
            if (original[y][x] == '#') {
                Point(x, y)
            } else {
                null
            }
        }
    }

    data class UnorderedPair<T>(val a: T, val b: T) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is UnorderedPair<*>) return false

            if (a != other.a && a != other.b) return false
            if (b != other.a && b != other.b) return false

            return true
        }

        override fun hashCode(): Int {
            return a.hashCode() + b.hashCode()
        }
    }

    val part1 = stars.asSequence().flatMap { a ->
        stars.asSequence().filter {
            it != a
        }.map { b ->
            UnorderedPair(a, b)
        }
    }.distinct().asStream().parallel().mapToInt { (a, b) ->
        return@mapToInt 0
        data class Entry(val p: Point, val distance: Int)

        val checked = mutableSetOf<Point>()
        val queue = ArrayDeque<Entry>()
        queue.add(Entry(a, 0))

        while (queue.isNotEmpty()) {
            val (p, distance) = queue.removeFirst()
            if (p.x < 0 || p.y < 0 || p.x >= original[0].length || p.y >= original.size) {
                continue
            }
            if (!checked.add(p)) {
                continue
            }
            if (p == b) {
                return@mapToInt distance
            }
            val len = if (p.x in expandedColumns || p.y in expandedRows) 2 else 1
            queue.add(Entry(Point(p.x + 1, p.y), distance + len))
            queue.add(Entry(Point(p.x - 1, p.y), distance + len))
            queue.add(Entry(Point(p.x, p.y + 1), distance + len))
            queue.add(Entry(Point(p.x, p.y - 1), distance + len))
        }

        error("No path found!!! WTF")
    }.sum()

    println("Part 1: $part1")

    val part2 = stars.asSequence().flatMap { a ->
        stars.asSequence().filter {
            it != a
        }.map { b ->
            UnorderedPair(a, b)
        }
    }.distinct().sumOf { (a, b) ->
        abs(a.x - b.x) + abs(a.y - b.y)
    }

    println("Part 2: $part2")
}