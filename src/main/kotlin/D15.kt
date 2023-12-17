import java.io.File

fun main() {
    val input = File("inputs/D15.txt").readText().split(',')

    fun hash(str: String): Int {
        var v = 0
        for (c in str) {
            v += c.code
            v *= 17
            v %= 256
        }
        return v
    }

    val part1 = input.sumOf { hash(it) }
    println("Part 1: $part1")

    data class Operation(val label: String, val focalLength: Int, val add: Boolean)

    val regex = """(\w+)([=\-])(\d?)""".toRegex()

    val ops = input.map {
        val (label, op, focalLengthStr) = regex.matchEntire(it)!!.destructured
        val focalLength = if (focalLengthStr.isEmpty()) -1 else focalLengthStr.toInt()
        Operation(label, focalLength, op == "=")
    }

    class Entry(val label: String, var focalLength: Int)

    val boxes = Array(256) { ArrayList<Entry>() }

    fun doOp(op: Operation) {
        if (op.add) {
            val hash = hash(op.label)
            val bucket = boxes[hash]
            val entry = bucket.find { it.label == op.label }
            if (entry != null) {
                entry.focalLength = op.focalLength
            } else {
                bucket.add(Entry(op.label, op.focalLength))
            }
        } else {
            if (op.focalLength == -1) {
                val hash = hash(op.label)
                val bucket = boxes[hash]
                bucket.removeIf { it.label == op.label }
            } else {
                val hash = hash(op.label)
                val bucket = boxes[hash]
                bucket.removeIf { it.label == op.label && it.focalLength == op.focalLength }
            }
        }
    }

    ops.forEach(::doOp)

    val part2 = boxes.withIndex().sumOf { (i, bucket) ->
        val boxPower = i + 1
        bucket.withIndex().sumOf { (slotIdx, e) ->
            boxPower * (slotIdx + 1) * e.focalLength
        }
    }
    println("Part 2: $part2")
}