package main.kotlin

class Day7 {
    val bidRegex = Regex("\\d+")
    val handRegex = Regex("(\\d|[A-Z])+")

    val handMap = mapOf(
        1 to "Highcard",
        2 to "One pair",
        3 to "Two pair",
        4 to "Three of a kind",
        5 to "Fullhouse",
        6 to "Four of a kind",
        7 to "Five of a kind"
    )

    val valueMap = mapOf (
        'T' to 10,
        'J' to 1,
        'Q' to 12,
        'K' to 13,
        'A' to 14
    )

    fun Char.getValue(): Int {
        return if (this.isDigit()) {
            this.digitToInt()
        } else {
            valueMap[this]!!
        }
    }

    fun compareHand(h1: String, h2: String): Int {
        h1.forEachIndexed { index, c ->
            when {
                c.getValue() > h2[index].getValue() -> {
                    return 1
                }
                c.getValue() < h2[index].getValue() -> {
                    return -1
                }
            }
        }
        return 0
    }

    fun getTheFissaTM(hand: String):Int {
        val grouped = hand.groupBy { it }
        when (grouped.toList().size) {
            1 -> {
                return 7
            }
            2 -> {
                return if (grouped.maxBy { it.value.size }.value.size == 4) {
                    6
                } else {
                    5
                }
            }
            3 -> {
                return if (grouped.maxBy { it.value.size }.value.size == 3) {
                    4
                } else {
                    3
                }
            }
            4 -> {
                return 2
            }
            else -> {
                return 1
            }
        }
    }

    fun doTheThing(): Long {
        val inputs = Day7::class.java.getResourceAsStream("/day7/test.txt").bufferedReader().readLines()
        val bidToHand = inputs.map { input ->
            val split = input.split(" ")
            val bid = bidRegex.findAll(split[1]).map { it.groupValues }.flatten().toList()[0].toLong()
            val hand = handRegex.findAll(split[0]).map { it.groupValues }.flatten().toList()[0]
            Pair(bid, hand)

        }
        val result = bidToHand
            .sortedWith (object : Comparator <Pair<Long, String>> {
                override fun compare (p0: Pair<Long, String>, pi: Pair<Long, String>) : Int {
                    val diff = getTheFissaTM(p0.second) - getTheFissaTM(pi.second)
                    return if (diff != 0) {
                        diff
                    } else {
                        compareHand(p0.second, pi.second)
                    }
                }
            })
            .mapIndexed { index, pair ->
                println("$pair * ${index + 1}")
                pair.first * (index + 1)
            }
            .sum()
        println(result)
        return result
    }

    val allPossibleValues = mutableListOf<Char>()
        .apply { this.addAll((2..9).map { it.toString().toCharArray()[0] })}
        .apply { this.addAll(valueMap.keys.filter { it != 'J' }) }

    fun getTheFissaTM2(hand: String):Int {
        var bestValue = getTheFissaTM(hand)
        allPossibleValues.forEach { newChar ->
            val newValue = getTheFissaTM(hand.replace('J', newChar))
            if (newValue > bestValue) bestValue = newValue
        }
        return bestValue
    }

    fun doTheOtherThing(): Long {
        val inputs = Day7::class.java.getResourceAsStream("/day7/input.txt").bufferedReader().readLines()
        val bidToHand = inputs.map { input ->
            val split = input.split(" ")
            val bid = bidRegex.findAll(split[1]).map { it.groupValues }.flatten().toList()[0].toLong()
            val hand = handRegex.findAll(split[0]).map { it.groupValues }.flatten().toList()[0]
            Pair(bid, hand)

        }
        val result = bidToHand
            .sortedWith (object : Comparator <Pair<Long, String>> {
                override fun compare (p0: Pair<Long, String>, pi: Pair<Long, String>) : Int {
                    val diff = getTheFissaTM2(p0.second) - getTheFissaTM2(pi.second)
                    return if (diff != 0) {
                        diff
                    } else {
                        compareHand(p0.second, pi.second)
                    }
                }
            })
            .mapIndexed { index, pair ->
                println("(${pair.second}, ${pair.first})")
                pair.first * (index + 1)
            }
            .sum()
        return result
    }
}

fun main() {
    println(Day7().doTheOtherThing())
}