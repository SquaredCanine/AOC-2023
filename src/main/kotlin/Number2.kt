package main.kotlin

class Number2 {

    val idRegex = Regex("\\d+:")
    val colorRegex = Regex(";*,*\\d+\\s(green|blue|red)")

    fun parseFile(): Int {
        val inputs = number1::class.java.getResourceAsStream("/assignment2/input.txt").bufferedReader().readLines()
        val constraint = Constraints(14, 12, 13)
        return inputs
            .map {
                val rounds = it.split(":")[1].split(";").map {
                    parseToRound(it)
                }
                Pair(it, Round(
                    rounds.maxOf { it.blue },
                    rounds.maxOf { it.green },
                    rounds.maxOf { it.red }
                ))
            }
            .map {
                it.second.red * it.second.blue * it.second.green
            }
            .sum()
    }

    fun extractNumberForColor(colorNumber: String): Int {
        return colorNumber.split(" ")[0].toInt()
    }

    fun parseToRound(input: String): Round {
        var red = 0
        var green = 0
        var blue = 0
        val result = colorRegex.findAll(input)
        result.forEach {
            val value = it.groups.get(0)!!.value
            val count = extractNumberForColor(value)
            when {
                value.contains("red") -> {
                    red = count
                }
                value.contains("blue") -> {
                    blue = count
                }
                else -> {
                    green = count
                }
            }
        }
        return Round(blue, red, green)
    }

    fun isValidRound(round: Round, constraint: Constraints): Boolean {
        return round.blue <= constraint.blue &&
                round.green <= constraint.green &&
                round.red <= constraint.red
    }

    data class Constraints(val blue: Int, val red: Int, val green: Int)
    data class Round(val blue: Int, val red: Int, val green: Int)
}

fun main() {
    println(Number2().parseFile())
}