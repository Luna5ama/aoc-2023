import java.io.File

fun main() {
    val input = File("inputs/D8.txt").readText()
    val lines = input.lines()

    val instruction = lines[0]

    data class Node(val name: String, var left: Node?, var right: Node?) {
        override fun toString(): String {
            return name
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Node) return false

            if (name != other.name) return false

            return true
        }

        override fun hashCode(): Int {
            return name.hashCode()
        }
    }

    val nodeRegex = """(...) = \((...), (...)\)""".toRegex()

    val nodeStr = lines.asSequence()
        .drop(2)
        .map {
            val (name, left, right) = nodeRegex.matchEntire(it)!!.destructured
            Triple(name, left, right)
        }
        .toList()

    val nodeMap = nodeStr.associate {
        it.first to Node(it.first, null, null)
    }

    for ((name, left, right) in nodeStr) {
        nodeMap[name]!!.left = nodeMap[left]
        nodeMap[name]!!.right = nodeMap[right]
    }

    run {
        var step = 0
        var node = nodeMap["AAA"]!!

        while (node.name != "ZZZ") {
            for (c in instruction) {
                if (c == 'L') {
                    node = node.left!!
                } else {
                    node = node.right!!
                }
                step++
                if (node.name == "ZZZ") {
                    break
                }
            }
        }

        println("Part 1: $step")
    }

    data class EndInfo(val sNode: Node, val sStep: Int, val eNode: Node, val steps: Int)

    val startTime = System.currentTimeMillis()
    val steps2zfromi = nodeMap.entries.associate { (k, v) ->
        k to Array(instruction.length) { it ->
            val starting = it
            var node = v
            var s1 = 0
            var steps = 0
            while (s1 < 1 || !node.name.endsWith("Z")) {
                for (c in instruction) {
                    s1++
                    if (s1 <= starting) continue

                    node = if (c == 'L') {
                        node.left!!
                    } else {
                        node.right!!
                    }
                    steps++
                    if (node.name.endsWith("Z")) {
                        break
                    }
                }
            }
            EndInfo(v, starting, node, steps)
        }
    }

    run {
        val nodes2 = nodeMap.values
            .filter { it.name.endsWith("A") }
            .toMutableList()
        val steps = LongArray(nodes2.size) { 0L }

        fun allEqual(): Boolean {
            val first = steps[0]
            if (first == 0L) {
                return false
            }
            for (i in 1 until steps.size) {
                if (steps[i] != first) {
                    return false
                }
            }
            return true
        }

        println("Running...")

        while (!allEqual()) {
            var i = -1
            var minSteps = Long.MAX_VALUE
            for (j in 0 until steps.size) {
                if (steps[j] < minSteps) {
                    minSteps = steps[j]
                    i = j
                }
            }
            check(i != -1)

            val sNode = nodes2[i]
            val step =  steps[i]
            val endInfo = steps2zfromi[sNode.name]!![(step % instruction.length).toInt()]
            nodes2[i] = endInfo.eNode
            steps[i] += endInfo.steps.toLong()
        }

        println("Part 2: ${steps[0]}")
    }
    val endTime = System.currentTimeMillis()
    println("Time: %.2f s".format((endTime - startTime) / 1000.0))
}