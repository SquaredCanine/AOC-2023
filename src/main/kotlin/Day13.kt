package main.kotlin

class Day13 {

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

    fun String.isMirrored(splitLine: Int): Boolean {
        val diff = this.length - splitLine
        val start = (0).coerceIn(splitLine - diff, splitLine)
        val end = (splitLine + splitLine).coerceIn(splitLine, this.length)
        val first = this.substring(start, splitLine)
        val second = this.substring(splitLine, end).reversed()
        if (first.isEmpty() || second.isEmpty()) {
            return false
        }
        return first == second
    }

    fun List<String>.findMirror(multiplicationFactor: Long): Long {
        for (i in 1 until this[0].length) {
            if (this.all { it.isMirrored(i) }) {
                return i * multiplicationFactor
            }
        }
        return 0
    }

    fun List<String>.mirrorMirrorOnTheWall(): Long {
        val column = this.findMirror(1)
        val row = this.transpose().findMirror(100)
        return if (row > 0) {
            row
        } else if (column > 0) {
            column
        } else {
            0
        }
    }

    fun doTheThing(): Long {
        val inputs = Util.getLinesFromFile("/day13/input.txt")
        val indices = inputs.mapIndexed { index, list -> if (list.isEmpty()) index else null }.filter { it != null }
        println(indices)
        val patterns = MutableList(indices.size + 1) { mutableListOf<String>() }
        var listIndex = 0
        inputs.forEachIndexed { index, list ->
            if (index in indices) {
                listIndex++
            } else {
                patterns[listIndex].add(list)
            }
        }
        return patterns.sumOf {
            it.mirrorMirrorOnTheWall()
        }
    }

    fun Char.flip(): String {
        return if (this == '#') {
            "."
        } else {
            "#"
        }
    }

    fun doTheOtherThing(): Long {
        val inputs = Util.getLinesFromFile("/day13/input.txt")
        val indices = inputs.mapIndexed { index, list -> if (list.isEmpty()) index else null }.filter { it != null }
        println(indices)
        val patterns = MutableList(indices.size + 1) { mutableListOf<String>() }
        var listIndex = 0
        inputs.forEachIndexed { index, list ->
            if (index in indices) {
                listIndex++
            } else {
                patterns[listIndex].add(list)
            }
        }
        return patterns.map {
            val originalValue = it.mirrorMirrorOnTheWall()
            val results = mutableListOf<Long>()
            it.forEachIndexed { xIndex, str ->
                str.forEachIndexed { yIndex, _ ->
                    val new = it.toMutableList()
                    val newValue = new[xIndex][yIndex].flip()
                    new[xIndex] = new[xIndex].replaceRange(yIndex, yIndex + 1, newValue)
                    val newLineValue = new.mirrorMirrorOnTheWall()
                    if (newLineValue != 0L) {
                        results.add(newLineValue)
                    }
                }
            }
            if (results.isEmpty()) {
                println("Empty")
                0L
            } else {
                if (results.all { it == results[0] }) {
                    println("Expected")
                    println(results)
                    results[0]
                } else {
                    println("PANIIIICCCCCCCCCCCC!!!!!!")
                    results
                        .filter { it != originalValue }
                        .also { println(it) }
                        .first()
                }
            }
        }
            .also {}
            .sum()
    }
}

fun main() {
    println(Day13().doTheThing())
//    println(Day13().doTheOtherThing())
}