import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import java.io.File
import kotlin.streams.asStream

fun main() {
    val input = File("inputs/D10.txt").readText()
    val lines = input.lines().toMutableList()

    val sy = lines.indexOfFirst { it.contains('S') }
    val sx = lines[sy].indexOf('S')

    lines[sy] = lines[sy].replace('S', '-')
//    lines[sy] = lines[sy].replace('S', 'F')
//    lines[sy] = lines[sy].replace('S', '7')

    val distanceGrid = Array(lines.size) { IntArray(lines[0].length) { Int.MAX_VALUE } }

    data class Point(val x: Int, val y: Int)
    data class FillEntry(val p: Point, val distance: Int)

    run {
        val checked = ObjectOpenHashSet<Point>()
        val queue = ArrayDeque<FillEntry>()

        queue.add(FillEntry(Point(sx, sy), 0))
        while (queue.isNotEmpty()) {
            val (p, distance) = queue.removeFirst()
            if (p.x < 0 || p.y < 0 || p.x >= lines[0].length || p.y >= lines.size) {
                continue
            }

            if (!checked.add(p)) {
                continue
            }

            distanceGrid[p.y][p.x] = distance

            val c = lines[p.y][p.x]
            fun tryAddNext(dx: Int, dy: Int, valids: String) {
                val x = p.x + dx
                val y = p.y + dy
                if (x < 0 || y < 0 || x >= lines[0].length || y >= lines.size) {
                    return
                }
                if (lines[y][x] !in valids) {
                    return
                }
                queue.add(FillEntry(Point(x, y), distance + 1))
            }
            when (c) {
                '-' -> {
                    tryAddNext(-1, 0, "-LF")
                    tryAddNext(1, 0, "-J7")

                }
                '|' -> {
                    tryAddNext(0, -1, "|F7")
                    tryAddNext(0, 1, "|LJ")
                }
                'L' -> {
                    tryAddNext(1, 0, "-J7")
                    tryAddNext(0, -1, "|7F")
                }
                'J' -> {
                    tryAddNext(-1, 0, "-LF")
                    tryAddNext(0, -1, "|7F")
                }
                '7' -> {
                    tryAddNext(-1, 0, "-LF")
                    tryAddNext(0, 1, "|LJ")
                }
                'F' -> {
                    tryAddNext(1, 0, "-J7")
                    tryAddNext(0, 1, "|LJ")
                }
            }
        }
    }

    distanceGrid.forEach { l ->
        for ((idx, e) in l.withIndex()) {
            if (e == Int.MAX_VALUE) {
                l[idx] = -1
            }
        }
    }

    val maxDistance = distanceGrid.maxOf { it.max() }
    println("Part 1: $maxDistance")

    val filterGrid = Array(lines.size) { y ->
        CharArray(lines[0].length) { x ->
            if (distanceGrid[y][x] == -1) {
                return@CharArray '.'
            }
            lines[y][x]
        }
    }

    val expanedGrid = Array(filterGrid.size * 3) { CharArray(filterGrid[0].size * 3) { '.' } }

    for (oy in filterGrid.indices) {
        for (ox in filterGrid[0].indices) {
            val ny = oy * 3
            val nx = ox * 3
            when (filterGrid[oy][ox]) {
                '.' -> {
                    // do nothing
                }
                '-' -> {
                    expanedGrid[ny + 1][nx + 0] = '-'
                    expanedGrid[ny + 1][nx + 1] = '-'
                    expanedGrid[ny + 1][nx + 2] = '-'
                }
                '|' -> {
                    expanedGrid[ny + 0][nx + 1] = '|'
                    expanedGrid[ny + 1][nx + 1] = '|'
                    expanedGrid[ny + 2][nx + 1] = '|'
                }
                'L' -> {
                    expanedGrid[ny + 0][nx + 1] = '|'
                    expanedGrid[ny + 1][nx + 1] = 'L'
                    expanedGrid[ny + 1][nx + 2] = '-'
                }
                'J' -> {
                    expanedGrid[ny + 0][nx + 1] = '|'
                    expanedGrid[ny + 1][nx + 1] = 'J'
                    expanedGrid[ny + 1][nx + 0] = '-'
                }
                '7' -> {
                    expanedGrid[ny + 1][nx + 0] = '-'
                    expanedGrid[ny + 1][nx + 1] = '7'
                    expanedGrid[ny + 2][nx + 1] = '|'
                }
                'F' -> {
                    expanedGrid[ny + 1][nx + 2] = '-'
                    expanedGrid[ny + 1][nx + 1] = 'F'
                    expanedGrid[ny + 2][nx + 1] = '|'
                }
            }
        }
    }

//    expanedGrid.forEach {
//        println(it.joinToString(""))
//    }

    fun connectsOutside(ox: Int, oy: Int): Boolean {
        val queue = ArrayDeque<Point>()
        val checked = ObjectOpenHashSet<Point>()

        queue.add(Point(ox * 3, oy * 3))

        while (queue.isNotEmpty()) {
            val p = queue.removeFirst()
            if (p.x < 0 || p.y < 0 || p.x >= expanedGrid[0].size || p.y >= expanedGrid.size) {
                return true
            }

            if (!checked.add(p)) {
                continue
            }

            if (expanedGrid[p.y][p.x] != '.') {
                continue
            }

            queue.add(Point(p.x - 1, p.y))
            queue.add(Point(p.x + 1, p.y))
            queue.add(Point(p.x, p.y - 1))
            queue.add(Point(p.x, p.y + 1))
        }
        return false
    }

    val part2 = lines.indices.asSequence().flatMap { y ->
        lines[y].indices.asSequence().map {
            Point(it, y)
        }
    }.asStream().parallel().filter { (x, y) ->
        filterGrid[y][x] == '.'
    }.filter {
        !connectsOutside(it.x, it.y)
    }.count()

    println("Part 2: $part2")
}