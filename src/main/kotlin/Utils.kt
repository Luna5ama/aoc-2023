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