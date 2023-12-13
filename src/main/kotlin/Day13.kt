package main.kotlin

import kotlin.math.abs

class Day13 {

    fun getTestInput(): List<List<String>> {
        val inputs = Util.getLinesFromFile("/day13/input.txt")
        val indices = inputs.mapIndexed { index, list -> if (list.isEmpty()) index else null }.filter { it != null }
        val patterns = MutableList(indices.size + 1) { mutableListOf<String>() }
        var listIndex = 0
        inputs.forEachIndexed { index, list ->
            if (index in indices) {
                listIndex++
            } else {
                patterns[listIndex].add(list)
            }
        }
        return patterns
    }

    fun List<String>.transpose(): List<String> {
        val cols = this[0].length
        val rows = this.size
        return List(cols) { j ->
            List(rows) { i ->
                this[i][j]
            }
        }.map {
            it.joinToString("")
        }
    }

    val String.hashTags: Int get() = this.count { it == '#' }

    fun String.isMirrored(splitLine: Int): Pair<Int, Boolean> {
        val diff = this.length - splitLine
        val start = (0).coerceIn(splitLine - diff, splitLine)
        val end = (splitLine + splitLine).coerceIn(splitLine, this.length)
        val first = this.substring(start, splitLine)
        val second = this.substring(splitLine, end).reversed()
        if (first.isEmpty() || second.isEmpty()) {
            return Pair(0, false)
        }
        return Pair(abs(first.hashTags - second.hashTags), first == second)
    }

    fun List<String>.findMirror(multiplicationFactor: Long, isSmudged: Boolean): Long {
        for (i in 1 until this[0].length) {
            var lenienceGiven = false
            if (this.all {
                    val result = it.isMirrored(i)
                    val smudgeCount = result.first
                    if (isSmudged) {
                        if (smudgeCount == 1 && !lenienceGiven) {
                            lenienceGiven = true
                            true
                        } else {
                            result.second
                        }
                    } else {
                        result.second
                    }
                } && (!isSmudged || lenienceGiven)) {
                return i * multiplicationFactor
            }
        }
        return 0
    }

    fun List<String>.mirrorMirrorOnTheWall(isSmudged: Boolean = false): Long {
        val column = this.findMirror(1, isSmudged)
        val row = this.transpose().findMirror(100, isSmudged)
        return if (row > 0) {
            row
        } else if (column > 0) {
            column
        } else {
            0
        }
    }

    fun doTheThing(): Long {
        val patterns = getTestInput()
        return patterns.sumOf {
            it.mirrorMirrorOnTheWall()
        }
    }

    fun doTheOtherThing(): Long {
        val patterns = getTestInput()
        return patterns.sumOf {
            it.mirrorMirrorOnTheWall(true)
        }
    }
}

fun main() {
    println(Day13().doTheThing())
    println(Day13().doTheOtherThing())
}