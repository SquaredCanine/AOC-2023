package main.kotlin

import kotlin.math.abs

class Day11 {

    val universeScale = 999999L

    fun doTheThing(): Int {
        var inputs = Util.getLinesFromFile("/day11/input.txt").toMutableList()
        //Expand Vertical
        val verticalIndices = mutableListOf<Int>()
        inputs.forEachIndexed { index, entry -> if (entry.all { it == '.' }) verticalIndices.add(index) }
        println(verticalIndices)
        //Expand Horizontal
        val horizontalIndices = mutableListOf<Int>()
        inputs[0].forEachIndexed { index, char ->
            val columnString = inputs.fold("") { acc, s -> acc + s[index] }
            if (columnString.all { it == '.' }) horizontalIndices.add(index)
        }
        println(horizontalIndices)
        inputs.forEach { println(it) }
        println("=========")
        horizontalIndices.reversed().forEach { index ->
            inputs = inputs.map {
                StringBuilder(it).insert(index, ".").toString()
            }.toMutableList()
        }
        inputs.forEach { println(it) }
        println("=========")
        verticalIndices.reversed().forEach { index ->
            inputs.add(index, ".".repeat(inputs[0].length))
        }
        inputs.forEach { println(it) }
        val coordinates = mutableListOf<Pair<Int, Int>>()
        inputs.forEachIndexed { yIndex, line ->
            line.forEachIndexed { xIndex, c ->
                if (c == '#') {
                    coordinates.add(Pair(yIndex, xIndex))
                }
            }
        }
        println(coordinates)
        return coordinates
            .mapIndexed { index, origin ->
                if (index == coordinates.size - 1) {
                    listOf<Int>()
                }
                coordinates.subList(index + 1, coordinates.size).map { destination ->
                    abs(destination.first - origin.first) + abs(destination.second - origin.second)
                }
            }
            .flatten()
            .sum()
    }

    fun List<String>.getHorizontalIndices(character: Char): List<Int> {
        val horizontalIndices = mutableListOf<Int>()
        this[0].forEachIndexed { index, char ->
            val columnString = this.fold("") { acc, s -> acc + s[index] }
            if (columnString.all { it == character }) horizontalIndices.add(index)
        }
        return horizontalIndices
    }

    fun List<String>.getVerticalIndices(character: Char): List<Int> {
        val verticalIndices = mutableListOf<Int>()
        this.forEachIndexed { index, entry -> if (entry.all { it == character }) verticalIndices.add(index) }
        return verticalIndices
    }

    fun doTheOtherThing(): Long {
        var inputs = Util.getLinesFromFile("/day11/input.txt").toMutableList()
        //Expand Vertical
        val verticalIndices = inputs.getVerticalIndices('.')
        println(verticalIndices)
        //Expand Horizontal
        val horizontalIndices = inputs.getHorizontalIndices('.')
        println(horizontalIndices)
        inputs.forEach { println(it) }
        println("=========")
        horizontalIndices.reversed().forEach { index ->
            inputs = inputs.map {
                StringBuilder(it).insert(index, "X").toString()
            }.toMutableList()
        }
        inputs.forEach { println(it) }
        println("=========")
        verticalIndices.reversed().forEach { index ->
            inputs.add(index, "X".repeat(inputs[0].length))
        }
        inputs.forEach { println(it) }
        val coordinates = mutableListOf<Pair<Int, Int>>()
        inputs.forEachIndexed { yIndex, line ->
            line.forEachIndexed { xIndex, c ->
                if (c == '#') {
                    coordinates.add(Pair(yIndex, xIndex))
                }
            }
        }
        val horizontalUniverseBorders = inputs.getHorizontalIndices('X')
        val verticalUniverseBorders = inputs.getVerticalIndices('X')

        return coordinates
            .mapIndexed { index, origin ->
                if (index == coordinates.size - 1) {
                    listOf<Int>()
                }

                coordinates.subList(index + 1, coordinates.size).map { destination ->
                    val horizontalScaler =
                        horizontalUniverseBorders.count { it in getPositiveRange(destination.second, origin.second) }
                    val verticalScaler =
                        verticalUniverseBorders.count { it in getPositiveRange(destination.first, origin.first) }
                    val hOffset = if (horizontalScaler > 0) horizontalScaler else 0
                    val vOffset = if (verticalScaler > 0) verticalScaler else 0
                    abs(destination.first - origin.first) +
                            abs(destination.second - origin.second) +
                            (horizontalScaler * universeScale - hOffset) +
                            (verticalScaler * universeScale - vOffset)
                }
            }
            .flatten()
            .sum()
    }

    fun getPositiveRange(a: Int, b: Int): IntRange {
        return if (a > b) {
            b..a
        } else {
            a..b
        }
    }
}

fun main() {
    //println(Day11().doTheThing())
    println(Day11().doTheOtherThing())
}