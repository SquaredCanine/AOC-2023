package main.kotlin

class Day6 {
    val digitRegex: Regex = Regex("\\d+")

    fun doTheThing(): Int {
        val inputs = Day6::class.java.getResourceAsStream("/day6/input.txt").bufferedReader().readLines()
        val times = listOf(digitRegex.findAll(inputs[0])
            .map { it.groupValues }
            .flatten()
            .fold("", {acc, i -> acc + i})
            .toLong())
        val distances = listOf(digitRegex.findAll(inputs[1])
            .map { it.groupValues }
            .flatten()
            .fold("", {acc, i -> acc + i})
            .toLong())
        println(times)
        println(distances)
        return times.mapIndexed { index, time ->
            calculateWinningPossibilities(time, distances[index])
        }.fold(1, {acc, i -> acc * i })
    }

    fun calculateWinningPossibilities(time: Long, distance: Long): Int {
        var winners = 0;
        for (i in 0..time) {
            val finalDistance = (time - i) * i
            if (finalDistance > distance) {
                winners++
            }
        }
        return winners
    }
}

fun main() {
    println(Day6().doTheThing())
}