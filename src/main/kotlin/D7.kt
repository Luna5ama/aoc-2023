import java.io.File

fun main() {
    val input = File("inputs/D7.txt").readText()

    data class Hand(val card: CharArray, val bid: Int) {
        override fun toString(): String {
            return "${card.concatToString()} $bid"
        }
    }

    fun parse(line: String): Hand {
        val split = line.split(" ")
        val card = split[0].toCharArray()
        val bit = split[1].toInt()
        return Hand(card, bit)
    }

    val hands = input.lines().map { parse(it) }

    val cardKinds = charArrayOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
    val cardStrength = cardKinds.reversed().mapIndexed { index, c -> c to index }.toMap()

    fun histo(hand: Hand): IntArray {
        val histo = IntArray(13)
        hand.card.forEach { card ->
            histo[cardStrength[card]!!]++
        }
        return histo
    }

    fun fiveOfAKind(histo: IntArray): Boolean {
        return histo.any { it == 5 }
    }

    fun fourOfAKind(histo: IntArray): Boolean {
        return histo.any { it == 4 }
    }

    fun fullHouse(histo: IntArray): Boolean {
        return histo.any { it == 3 } && histo.any { it == 2 }
    }

    fun threeOfAKind(histo: IntArray): Boolean {
        return histo.any { it == 3 } && histo.count { it == 1 } == 2
    }

    fun twoPair(histo: IntArray): Boolean {
        return histo.count { it == 2 } == 2 && histo.count { it == 1 } == 1
    }

    fun onePair(histo: IntArray): Boolean {
        return histo.count { it == 2 } == 1 && histo.count { it == 1 } == 3
    }

    fun highCard(histo: IntArray): Boolean {
        return histo.count { it == 1 } == 5
    }

    fun handStrength(histo: IntArray): Int {
        return when {
            fiveOfAKind(histo) -> 9
            fourOfAKind(histo) -> 8
            fullHouse(histo) -> 7
            threeOfAKind(histo) -> 6
            twoPair(histo) -> 5
            onePair(histo) -> 4
            highCard(histo) -> 3
            else -> throw RuntimeException("Invalid hand")
        }
    }

    val part1 = hands.sortedWith(
        compareBy<Hand> { handStrength(histo(it)) }
            .thenBy { cardStrength[it.card[0]]!! }
            .thenBy { cardStrength[it.card[1]]!! }
            .thenBy { cardStrength[it.card[2]]!! }
            .thenBy { cardStrength[it.card[3]]!! }
            .thenBy { cardStrength[it.card[4]]!! }
    ).mapIndexed { index, hand ->
        hand.bid * (index + 1)
    }.sum()

    println("Part 1: $part1")

    fun handStrength2(histo: IntArray): Int {
        return sequence {
            yield(histo)
            val ji = cardStrength['J']!!
            val jCount = histo[ji]
            for (i in 0..<13) {
                if (i == ji) continue
                if (histo[i] == 0) continue
                val new = histo.copyOf()
                new[ji] = 0
                new[i] += jCount
                yield(new)
            }
        }.mapNotNull {
            when {
                fiveOfAKind(it) -> 9
                fourOfAKind(it) -> 8
                fullHouse(it) -> 7
                threeOfAKind(it) -> 6
                twoPair(it) -> 5
                onePair(it) -> 4
                highCard(it) -> 3
                else -> null
            }
        }.max()
    }

    val cardStrength2 = mapOf(
        'A' to 12,
        'K' to 11,
        'Q' to 10,
        'T' to 9,
        '9' to 8,
        '8' to 7,
        '7' to 6,
        '6' to 5,
        '5' to 4,
        '4' to 3,
        '3' to 2,
        '2' to 1,
        'J' to 0,
    )

    val part2 = hands.sortedWith(
        compareBy<Hand> { handStrength2(histo(it)) }
            .thenBy { cardStrength2[it.card[0]]!! }
            .thenBy { cardStrength2[it.card[1]]!! }
            .thenBy { cardStrength2[it.card[2]]!! }
            .thenBy { cardStrength2[it.card[3]]!! }
            .thenBy { cardStrength2[it.card[4]]!! }
    ).mapIndexed { index, hand ->
        hand.bid * (index + 1)
    }.sum()

    println("Part 2: $part2")
}