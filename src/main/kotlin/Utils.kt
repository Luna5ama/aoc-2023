fun permutationIndices(fromN: Int, n: Int): Sequence<IntArray> {
    val states = IntArray(n)

    return sequence {
        outer@ while (true) {
            yield(states.copyOf())

            states[0]++
            for (i in states.indices) {
                if (states[i] < fromN) continue
                if (i == states.lastIndex) break@outer
                states[i] = 0
                states[i + 1]++
            }
        }
    }
}

enum class Direction4(val dx: Int, val dy: Int, val oppositeIndex: Int) {
    UP(0, -1, 2),
    RIGHT(1, 0, 3),
    DOWN(0, 1, 0),
    LEFT(-1, 0, 1);

    val opposite get() = entries[oppositeIndex]
}