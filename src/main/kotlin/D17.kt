import java.io.File
import java.util.*

fun main() {
    val input = File("inputs/D17.txt").readText()
    val grid = input.lines().map { line ->
        line.map { it.digitToInt() }.toIntArray()
    }

    val ySize = grid.size
    val xSize = grid[0].size

    data class Point(val x: Int, val y: Int) {
        operator fun plus(dir: Direction4) = Point(x + dir.dx, y + dir.dy)
    }
    data class ExploredRecord(val p: Point, val dir: Direction4, val straightSteps: Int)
    data class Entry(val p: Point, val dir: Direction4, val straightSteps: Int, val loss: Int): Comparable<Entry> {
        override fun compareTo(other: Entry): Int {
            return loss.compareTo(other.loss)
        }
    }

    val queue = PriorityQueue<Entry>()
    val visited = mutableSetOf<ExploredRecord>()

    val end = Point(xSize - 1, ySize - 1)

    fun tryAdd(p: Point, dir: Direction4, straightSteps: Int, loss: Int) {
        val np = p + dir
        if (np.x !in 0..<xSize || np.y !in 0..<ySize) return
        val exploredRecord = ExploredRecord(np, dir, straightSteps)
        if (!visited.add(exploredRecord)) return
        val gridLoss = grid[np.y][np.x]
        val entry = Entry(np, dir, straightSteps, loss + gridLoss)
        queue.add(entry)
    }

    fun tryAddInit(entry: Entry) {
        if (entry.p.x !in 0..<xSize || entry.p.y !in 0..<ySize) return
        val exploredRecord = ExploredRecord(entry.p, entry.dir, entry.straightSteps)
        if (!visited.add(exploredRecord)) return
        queue.add(entry)
    }

    tryAddInit(Entry(Point(0, 0), Direction4.RIGHT, 1, 0))
    tryAddInit(Entry(Point(0, 0), Direction4.DOWN, 1, 0))

    while (queue.isNotEmpty()) {
        val entry = queue.poll()
        if (entry.p == end) {
            println("Part 1: ${entry.loss}")
            break
        }
        if (entry.straightSteps < 10) {
            tryAdd(entry.p, entry.dir, entry.straightSteps + 1, entry.loss)
        }
        if (entry.straightSteps >= 4) {
            for (dir in Direction4.entries) {
                if (dir == entry.dir || dir == entry.dir.opposite) continue
                tryAdd(entry.p, dir, 1, entry.loss)
            }
        }
    }
}