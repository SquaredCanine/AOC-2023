package main.kotlin

import java.io.File

class number1 {
    private val numbers = listOf(
        "one",
        "two",
        "three",
        "four",
        "five",
        "six",
        "seven",
        "eight",
        "nine"
    )
    private val digitRegex = Regex("\\d")
    private val wordRegexes = numbers.map { Regex(it) }

    fun parseFile(): Int {
        val inputs = number1::class.java.getResourceAsStream("/input.txt").bufferedReader().readLines()
        val parsed = inputs.map { input ->
            val digits = digitRegex.findAll(input).toList()
            val words = wordRegexes.map { it.findAll(input).toList() }.flatten()

            val combined: MutableList<MatchGroup> = mutableListOf()
            (digits + words).forEach { it ->
                combined.addAll(it.groups.map { it!! })
            }
            combined.sortBy { it.range.first }

            val firstDigit: Char = parseMatchGroup(combined.first())
            val secondDigit: Char = parseMatchGroup(combined.last())

            (firstDigit + "" + secondDigit).toInt()
        }
        return parsed.sum()
    }

    fun parseMatchGroup(group: MatchGroup): Char {
        return if (group.value.length > 1) {
            ("" + (numbers.indexOf(group.value) + 1))[0]
        } else {
            group.value[0]
        }
    }
}

fun main() {
    println(number1().parseFile())
}