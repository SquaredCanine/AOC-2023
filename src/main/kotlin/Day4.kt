package main.kotlin

class Day4 {
    val digitRegex = Regex("\\d+")

    fun doTheThing(): Int {
        val inputs = number1::class.java.getResourceAsStream("/day4/input.txt").bufferedReader().readLines()
        return inputs.map { line ->
            val numberSplit = line.split(":")[1]
            val parsedNumbers = numberSplit.split("|").map { numberString ->
                digitRegex.findAll(numberString).map {
                    it.groupValues.map { it.toInt() }
                }.flatten().toList()
            }
            val pair = Pair(parsedNumbers[0], parsedNumbers[1])
            println(pair)
            pair.first.fold(0, { acc, input ->
                if (pair.second.contains(input)) {
                    if (acc == 0) {
                        1
                    } else {
                        acc * 2
                    }
                } else {
                    acc
                }
            })
        }.sum()
    }

    fun doTheOtherThing(): Int {
        val inputs = number1::class.java.getResourceAsStream("/day4/input.txt").bufferedReader().readLines()
        val cardNumbers = inputs.map { 1 }.toMutableList()
        inputs.forEachIndexed { gameIndex, line ->
            val numberSplit = line.split(":")[1]
            val parsedNumbers = numberSplit.split("|").map { numberString ->
                digitRegex.findAll(numberString).map {
                    it.groupValues.map { it.toInt() }
                }.flatten().toList()
            }
            val pair = Pair(parsedNumbers[0], parsedNumbers[1])
            pair.first.fold(0, { acc, input ->
                if (pair.second.contains(input)) {
                    updateTheCards(cardNumbers, gameIndex + acc + 1, cardNumbers[gameIndex])
                    acc + 1
                } else {
                    acc
                }
            })
        }
        return cardNumbers.sum()
    }

    fun updateTheCards(numberList: MutableList<Int>, index: Int, increment: Int): List<Int> {
        numberList[index] = numberList[index] + increment
        return numberList
    }
}

fun main() {
    println(Day4().doTheThing())
    println(Day4().doTheOtherThing())
}