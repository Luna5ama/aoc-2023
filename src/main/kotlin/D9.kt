import java.io.File

fun main() {
    val input = File("inputs/D9.txt").readText()
    val lines = input.lines()

    val part1 = lines.sumOf { lineStr ->
        val nums = lineStr.split(' ').map { it.toInt() }
        val arrays = mutableListOf<IntArray>()
        arrays.add(IntArray(nums.size) { nums[it] })

        while (true) {
            var allZero = true
            val prevArray = arrays.last()
            val newArray = IntArray(prevArray.size - 1)
            for (i in 0 until prevArray.size - 1) {
                newArray[i] = prevArray[i + 1] - prevArray[i]
                if (newArray[i] != 0) {
                    allZero = false
                }
            }
            arrays.add(newArray)
            if (allZero) {
                break
            }
        }

        val resultWork = IntArray(arrays.size)

        for (i in (arrays.lastIndex - 1) downTo 0) {
            resultWork[i] = resultWork[i + 1] + arrays[i].last()
        }

       resultWork.first()
    }

    println("Part 1: $part1")



    val part2 = lines.sumOf { lineStr ->
        val nums = lineStr.split(' ').map { it.toInt() }
        val arrays = mutableListOf<IntArray>()
        arrays.add(IntArray(nums.size) { nums[it] })

        while (true) {
            var allZero = true
            val prevArray = arrays.last()
            val newArray = IntArray(prevArray.size - 1)
            for (i in 0 until prevArray.size - 1) {
                newArray[i] = prevArray[i + 1] - prevArray[i]
                if (newArray[i] != 0) {
                    allZero = false
                }
            }
            arrays.add(newArray)
            if (allZero) {
                break
            }
        }

        val resultWork = IntArray(arrays.size)

        for (i in (arrays.lastIndex - 1) downTo 0) {
            resultWork[i] = arrays[i].first() - resultWork[i + 1]
        }

        println(resultWork.contentToString())
        resultWork.first()
    }

    println("Part 2: $part2")
}