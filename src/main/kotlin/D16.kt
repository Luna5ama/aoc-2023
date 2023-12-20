import java.io.File

fun main() {
    val grid = File("inputs/D16.txt").readLines()
    val ySize = grid.size
    val xSize = grid[0].length

    data class Entry(val x: Int, val y: Int, val dir: Direction4)

    fun simulate(initEntry: Entry): Int {

        val beamState = Array(ySize) { Array(xSize) { BooleanArray(4) } }
        val queue = ArrayDeque<Entry>()
        fun lightWalk(sx: Int, sy: Int, sd: Direction4) {
            var x = sx
            var y = sy
            var dir = sd
            while (x in 0..<xSize && y in 0..<ySize) {
                if (beamState[y][x][dir.ordinal]) {
                    break
                }
                beamState[y][x][dir.ordinal] = true
                when (grid[y][x]) {
                    '.' -> {
                        // do nothing
                    }
                    '/' -> {
                        dir = when (dir) {
                            Direction4.UP -> Direction4.RIGHT
                            Direction4.RIGHT -> Direction4.UP
                            Direction4.DOWN -> Direction4.LEFT
                            Direction4.LEFT -> Direction4.DOWN
                        }
                    }
                    '\\' -> {
                        dir = when (dir) {
                            Direction4.UP -> Direction4.LEFT
                            Direction4.RIGHT -> Direction4.DOWN
                            Direction4.DOWN -> Direction4.RIGHT
                            Direction4.LEFT -> Direction4.UP
                        }
                    }
                    '|' -> {
                        when (dir) {
                            Direction4.UP, Direction4.DOWN -> {
                                // do nothing
                            }
                            Direction4.LEFT, Direction4.RIGHT -> {
                                dir = Direction4.UP
                                queue.add(Entry(x, y + 1, Direction4.DOWN))
                            }
                        }
                    }
                    '-' -> {
                        when (dir) {
                            Direction4.UP, Direction4.DOWN -> {
                                dir = Direction4.RIGHT
                                queue.add(Entry(x - 1, y, Direction4.LEFT))
                            }
                            Direction4.LEFT, Direction4.RIGHT -> {
                                // do nothing
                            }
                        }
                    }
                }
                x += dir.dx
                y += dir.dy
            }
        }

        queue.add(initEntry)

        while (queue.isNotEmpty()) {
            val (x, y, dir) = queue.removeFirst()
            lightWalk(x, y, dir)
        }

        return beamState.sumOf { row ->
            row.count { it.contains(true) }
        }
    }

    val part1 = simulate(Entry(0, 0, Direction4.RIGHT))

    println("Part 1: $part1")

    val part2 = sequence {
        for (y in 0..<ySize) {
            yield(Entry(0, y, Direction4.RIGHT))
            yield(Entry(xSize - 1, y, Direction4.LEFT))
        }

        for (x in 0..<xSize) {
            yield(Entry(x, 0, Direction4.DOWN))
            yield(Entry(x, ySize - 1, Direction4.UP))
        }
    }.maxOf {
        simulate(it)
    }

    println("Part 2: $part2")
}