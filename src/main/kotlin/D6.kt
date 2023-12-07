fun main() {
    val input = """Time:        50     74     86     85
Distance:   242   1017   1691   1252"""
    val numberRegex = """[0-9]+""".toRegex()

    val lines = input.lines()
    val times = numberRegex.findAll(lines[0]).map { it.value.toInt() }.toList()
    val distances = numberRegex.findAll(lines[1]).map { it.value.toInt() }.toList()

    fun calc(totalTime: Int, pressTime: Int): Int {
        return pressTime * (totalTime - pressTime)
    }

    val nums = times.asSequence().zip(distances.asSequence()).map { (time, recordDistance) ->
        (1..<time).asSequence().map { pressTime ->
            calc(time, pressTime)
        }.filter {
            it > recordDistance
        }.count()
    }.toList()

    var part1 = 1
    for (e in nums) {
        part1 *= e
    }
    println(part1)

    val times2 = lines[0].filter { it.isDigit() }.toLong()
    val distances2 = lines[1].filter { it.isDigit() }.toLong()

    fun calc(totalTime: Long, pressTime: Long): Long {
        return pressTime * (totalTime - pressTime)
    }

    val part2 = (1..<times2).asSequence().map { pressTime ->
        calc(times2, pressTime)
    }.filter {
        it > distances2
    }.count()

    println(part2)
}