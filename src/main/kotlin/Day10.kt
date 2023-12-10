package main.kotlin

import main.kotlin.Day10.WindDirection.*

class Day10 {

    enum class WindDirection { NORTH, SOUTH, EAST, WEST }

    fun WindDirection.invert(): WindDirection {
        return when (this) {
            NORTH -> SOUTH
            SOUTH -> NORTH
            EAST -> WEST
            WEST -> EAST
        }
    }

    data class Coordinate(val x: Int, val y: Int)

    fun isValidMove(direction: WindDirection, piece: Char): Pair<WindDirection, WindDirection>? {
        return translationMap[piece]?.find { it.first == direction }
    }

    val translationMap = mapOf(
        '|' to listOf(Pair(NORTH, SOUTH), Pair(SOUTH, NORTH)),
        '-' to listOf(Pair(EAST, WEST), Pair(WEST, EAST)),
        'L' to listOf(Pair(NORTH, EAST), Pair(EAST, NORTH)),
        'J' to listOf(Pair(NORTH, WEST), Pair(WEST, NORTH)),
        '7' to listOf(Pair(WEST, SOUTH), Pair(SOUTH, WEST)),
        'F' to listOf(Pair(EAST, SOUTH), Pair(SOUTH, EAST)),
    )

    val expansionMap = mapOf(
        '|' to listOf(" X ", " X ", " X "),
        '-' to listOf("   ", "XXX", "   "),
        'L' to listOf(" X ", " XX", "   "),
        'J' to listOf(" X ", "XX ", "   "),
        '7' to listOf("   ", "XX ", " X "),
        'F' to listOf("   ", " XX", " X "),
        'F' to listOf("   ", " XX", " X "),
        'S' to listOf(" X ", " X ", " X "),
        '.' to listOf("   ", "   ", "   "),
        ' ' to listOf("   ", "   ", "   ")
    )

    fun doTheThing(): Int {
        val inputs = Util.getLinesFromFile("/day10/input.txt")
        var yIndex = inputs.indexOfFirst { it.contains("S") }
        var xIndex = inputs[yIndex].indexOf('S')
        var steps = 1
        val initialMoves = listOf(
            Pair(WEST, inputs[yIndex][xIndex + 1]),
            Pair(EAST, inputs[yIndex][xIndex - 1]),
            Pair(NORTH, inputs[yIndex + 1][xIndex]),
            Pair(SOUTH, inputs[yIndex - 1][xIndex])
        ).map {
            isValidMove(it.first, it.second)
        }.filterNotNull()
        var nextMove = initialMoves[0]
        while (true) {
            when (nextMove.second) {
                NORTH -> {
                    yIndex--
                }

                SOUTH -> {
                    yIndex++
                }

                EAST -> {
                    xIndex++
                }

                WEST -> {
                    xIndex--
                }
            }
            if (inputs[yIndex][xIndex] == 'S') {
                return steps
            }
            nextMove = isValidMove(nextMove.second.invert(), inputs[yIndex][xIndex])!!
            steps++
//            val progress = inputs.toMutableList()
//            progress[yIndex] = progress[yIndex].replaceRange(xIndex, xIndex + 1, "X")
//            progress.forEach { println(it) }
//            println("=========")
        }
    }

    fun getSurroundingIncides(curr: Coordinate, baseAmaze: List<String>): List<Coordinate> {
        val list = mutableListOf<Coordinate>()
        if ((curr.x + 1) < baseAmaze[0].length) list.add(Coordinate(curr.x + 1, curr.y))
        if (curr.x >= 1) list.add(Coordinate(curr.x - 1, curr.y))
        if ((curr.y + 1) < baseAmaze.size) list.add(Coordinate(curr.x, curr.y + 1))
        if (curr.y >= 1) list.add(Coordinate(curr.x, curr.y - 1))
        return list
    }

    fun List<String>.countSpaces(): Int {
        var count = 0
        for (i in 0 until this.size step 3) {
            for (x in 0 until this[i].length step 3) {
                if ((this[i].substring(x..x+2) == "   ") &&
                    (this[i+1].substring(x..x+2) == "   ") &&
                    (this[i+2].substring(x..x+2) == "   ")
                ) {
                    count++
                }
            }
        }
        return count
    }

    fun List<String>.countDashes(): Int {
        return this.sumOf { it.count { char -> char == '-' } }
    }

    fun fillTheVoid(baseMap: MutableList<String>): List<String> {
        val expandedMap = mutableListOf<String>()
        baseMap.forEach { println(it) }
        baseMap.forEachIndexed { index, string ->
            val translatedIndex = index * 3
            expandedMap.addAll(listOf("", "", ""))
            string.forEach {
                val newValues = expansionMap[it]!!
                newValues.forEachIndexed { expansionIndex, newValue ->
                    expandedMap[translatedIndex + expansionIndex] =
                        expandedMap[translatedIndex + expansionIndex] + newValue
                }
            }
        }
        expandedMap.forEach { println(it) }
        expandedMap[0] = expandedMap[0].replace(" ", "-")
        expandedMap[expandedMap.size - 1] = expandedMap[expandedMap.size - 1].replace(" ", "-")
        expandedMap.replaceAll {
            var newString = it
            if (it.last() == ' ') newString = newString.replaceRange(newString.length - 1, newString.length, "-")
            if (it.first() == ' ') newString = newString.replaceRange(0, 1, "-")
            newString
        }
        var dashCount = expandedMap.countDashes()
        while (true) {
            for(yIndex in 0 until expandedMap.size) {
                for(xIndex in 0 until expandedMap[yIndex].length) {
                    if (expandedMap[yIndex][xIndex] == '-') {
                        val indices = getSurroundingIncides(Coordinate(xIndex, yIndex), expandedMap)
                        indices.forEach { coordinate ->
                            if (expandedMap[coordinate.y][coordinate.x] == ' ') expandedMap[coordinate.y] =
                                expandedMap[coordinate.y].replaceRange(coordinate.x, coordinate.x + 1, "-")
                        }
                    }
                }
            }
            if (expandedMap.countDashes() == dashCount) {
                return expandedMap
            } else {
                dashCount = expandedMap.countDashes()
            }
        }
    }

    fun doTheOtherThing(): Int {
        val inputs = Util.getLinesFromFile("/day10/input.txt")
        var yIndex = inputs.indexOfFirst { it.contains("S") }
        var xIndex = inputs[yIndex].indexOf('S')
        var steps = 1
        val initialMoves = listOf(
            Pair(WEST, inputs[yIndex][xIndex + 1]),
            Pair(EAST, inputs[yIndex][xIndex - 1]),
            Pair(NORTH, inputs[yIndex + 1][xIndex]),
        ).map {
            isValidMove(it.first, it.second)
        }.filterNotNull()
        val visitedParts = mutableListOf(
            Coordinate(xIndex, yIndex)
        )
        var nextMove = initialMoves[0]
        while (true) {
            when (nextMove.second) {
                NORTH -> {
                    yIndex--
                }

                SOUTH -> {
                    yIndex++
                }

                EAST -> {
                    xIndex++
                }

                WEST -> {
                    xIndex--
                }
            }
            visitedParts.add(Coordinate(xIndex, yIndex))
            if (inputs[yIndex][xIndex] == 'S') {
                val progress = inputs.toMutableList()
                progress.replaceAll {
                    " ".repeat(it.length)
                }
                visitedParts.forEach {
                    progress[it.y] = progress[it.y].replaceRange(it.x, it.x + 1, inputs[it.y][it.x].toString())
                }
                println(visitedParts.size)
                return fillTheVoid(progress)
                    .also {
                        it.forEach { println(it) }
                    }
                    .also { println(visitedParts.size) }
                    .countSpaces()
            }
            nextMove = isValidMove(nextMove.second.invert(), inputs[yIndex][xIndex])!!
            steps++
        }
    }
}

fun main() {
    println(Day10().doTheThing() / 2)
    println(Day10().doTheOtherThing())
}