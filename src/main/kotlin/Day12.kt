package main.kotlin

import kotlin.system.measureTimeMillis

class Day12 {

    val possibilities = listOf('.', '#')

    data class Entry(val springConditionRow: String, val engineerNotes: List<Int>)

    val cache = mutableMapOf<String, Long>()

    fun String.expandTheSprings(): String {
        val (row, values) = this.splitForParsing()
        val engineerNotes = List(5) { values }.flatten()
        val springs = List(5) { row }
        return "${springs.joinToString("?")} ${engineerNotes.joinToString(",")}"
    }

    fun String.splitForParsing(): Entry {
        val split = this.split(" ")
        return Entry(
            split[0],
            Util.numberRegex.findAll(split[1]).map { it.groupValues }.flatten().map { it.toInt() }.toList()
        )
    }

    fun numberOfSolutions(remainder: String, groups: List<Int>, groupSize: Int = 0): Long =
        cache.getOrPut("$remainder$groups$groupSize") {
            if (remainder.isEmpty()) {
                return if (groups.isEmpty() && groupSize == 0) {
                    1
                } else {
                    0
                }
            }
            val nextCharacter = remainder.first()
            val nextCharacters = if (nextCharacter == '?') possibilities else listOf(nextCharacter)

            nextCharacters.map {
                if (it == '#') {
                    numberOfSolutions(remainder.substring(1), groups, groupSize + 1)
                } else {
                    if (groupSize > 0) {
                        if (groups.isNotEmpty() && groups.first() == groupSize) {
                            numberOfSolutions(remainder.substring(1), groups.subList(1, groups.size))
                        } else {
                            0
                        }
                    } else {
                        numberOfSolutions(remainder.substring(1), groups)
                    }
                }
            }.sum()
        }

    fun doTheThing(): Long {
        val inputs = Util.getLinesFromFile("/day12/input.txt")
        return inputs.sumOf {
            val entry = it.splitForParsing()
            numberOfSolutions("${entry.springConditionRow}.", entry.engineerNotes)
        }
    }

    fun doTheOtherThing(): Long {
        val inputs = Util.getLinesFromFile("/day12/input.txt")
        return inputs.sumOf {
            val entry = it.expandTheSprings().splitForParsing()
            val result = numberOfSolutions("${entry.springConditionRow}.", entry.engineerNotes)
            result
        }
    }
}

fun main() {
    val part1 = measureTimeMillis {
        println(Day12().doTheThing())
    }
    val part2 = measureTimeMillis {
        println(Day12().doTheOtherThing())
    }
    println("Part 1 in $part1 ms")
    println("Part 2 in $part2 ms")
}

