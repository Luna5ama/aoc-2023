import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import java.io.File
import kotlin.time.measureTime

object D14 {
    private var xSize = 0
    private var ySize = 0

    private operator fun CharArray.get(y: Int, x: Int): Char {
        return this[y * xSize + x]
    }

    private operator fun CharArray.set(y: Int, x: Int, value: Char) {
        this[y * xSize + x] = value
    }

    private fun moveNorth(state: GridState): GridState {
        val grid = state.grid.copyOf()

        for (ix in 0..<xSize) {
            for (iy in 1..<ySize) {
                val c = grid[iy, ix]
                if (c == '.') continue
                if (c == '#') continue
                var y2 = iy
                while (y2 >= 1 && grid[y2 - 1, ix] == '.') {
                    y2--
                }
                grid[iy, ix] = '.'
                grid[y2, ix] = 'O'
            }
        }

        return GridState(grid)
    }

    private fun moveSouth(state: GridState): GridState {
        val grid = state.grid.copyOf()

        for (ix in 0..<xSize) {
            for (iy in ySize - 2 downTo 0) {
                val c = grid[iy, ix]
                if (c == '.') continue
                if (c == '#') continue
                var y2 = iy
                while (y2 <= ySize - 2 && grid[y2 + 1, ix] == '.') {
                    y2++
                }
                grid[iy, ix] = '.'
                grid[y2, ix] = 'O'
            }
        }

        return GridState(grid)
    }

    private fun moveEast(state: GridState): GridState {
        val grid = state.grid.copyOf()

        for (iy in 0..<ySize) {
            for (ix in xSize - 2 downTo 0) {
                val c = grid[iy, ix]
                if (c == '.') continue
                if (c == '#') continue
                var x2 = ix
                while (x2 <= xSize - 2 && grid[iy, x2 + 1] == '.') {
                    x2++
                }
                grid[iy, ix] = '.'
                grid[iy, x2] = 'O'
            }
        }

        return GridState(grid)
    }

    private fun moveWest(state: GridState): GridState {
        val grid = state.grid.copyOf()

        for (iy in 0..<ySize) {
            for (ix in 1..<xSize) {
                val c = grid[iy, ix]
                if (c == '.') continue
                if (c == '#') continue
                var x2 = ix
                while (x2 >= 1 && grid[iy, x2 - 1] == '.') {
                    x2--
                }
                grid[iy, ix] = '.'
                grid[iy, x2] = 'O'
            }
        }

        return GridState(grid)
    }

    private fun resultSum(state: GridState): Int {
        var sum = 0
        for (iy in 0..<ySize) {
            val mul = ySize - iy
            for (ix in 0..<xSize) {
                if (state.grid[iy, ix] != 'O') continue
                sum += mul
            }
        }
        return sum
    }

    private val gridCache4 = GridCacheMap()
    private val gridCacheNorth = GridCacheMap()
    private val gridCacheWest = GridCacheMap()
    private val gridCacheSouth = GridCacheMap()
    private val gridCacheEast = GridCacheMap()

    private fun cycle(state: GridState): GridState {
        val cache4 = gridCache4[state]
        if (cache4 != null) {
            return cache4
        }

        var s = state
        s = gridCacheNorth.getOrPut(s) { moveNorth(s) }
        s = gridCacheWest.getOrPut(s) { moveWest(s) }
        s = gridCacheSouth.getOrPut(s) { moveSouth(s) }
        s = gridCacheEast.getOrPut(s) { moveEast(s) }
        gridCache4[state] = s
        return s
    }

    private fun printGrid(grid: CharArray) {
        for (iy in 0..<ySize) {
            for (ix in 0..<xSize) {
                print(grid[iy, ix])
            }
            println()
        }
    }

    private class GridState(val grid: CharArray) {
        val hashCode = grid.contentHashCode()

        override fun hashCode(): Int {
            return hashCode
        }

        override fun equals(other: Any?): Boolean {
            other as GridState
            if (other === this) return true
            return grid.contentEquals(other.grid)
        }
    }

    private class GridCacheMap : Object2ObjectOpenHashMap<GridState, GridState>(1024, 0.5f)

    private fun List<String>.toGridState(): GridState {
        return GridState(flatMap { it.toList() }.toCharArray())
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val input = File("inputs/D14.txt").readLines()

        xSize = input[0].length
        ySize = input.size

        var part1Grid = input.toGridState()

        part1Grid = moveNorth(part1Grid)

        val part1 = resultSum(part1Grid)

        println("Part 1: $part1")

        var part2grid = input.toGridState()

        val times = 1_000_000_000
        val time = measureTime {
            repeat(times) {
                part2grid = cycle(part2grid)
            }
        }

        println("%.2fs".format(time.inWholeMilliseconds / 1000.0))

        val part2 = resultSum(part2grid)
        println("Part 2: $part2")
    }
}