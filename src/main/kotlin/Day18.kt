package main.kotlin

import kotlin.math.floor
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

class Day18 {

    val bracketRegex = Regex("\\(|\\)")

    data class Entry(val direction: String, val length: Long, val colorCode: String)
    data class Coordinate(val x: Long, val y: Long)

    fun calculateCubicMeters(nodes: List<Coordinate>, edge: Long): Long {
        val combinations = nodes.zipWithNext()
        val result = combinations.fold(0L) { acc, pair ->
            acc + (pair.first.x * pair.second.y) - (pair.first.y * pair.second.x)
        }
        val interiorArea = (result / 2L) - (edge / 2L) + 1
        return interiorArea + edge
    }

    fun part2(): Long {
        val input = getInput2()
        val nodes = mutableListOf<Coordinate>()
        var xIndex = 1L
        var yIndex = 1L
        var edge = 0L
        input.forEach {
            nodes.add(Coordinate(xIndex, yIndex))
            when(it.direction) {
                "0" -> { xIndex += it.length}
                "1" -> { yIndex += it.length}
                "3" -> { yIndex -= it.length}
                else -> { xIndex -= it.length}
            }
            edge += it.length
        }
        nodes.add(Coordinate(xIndex, yIndex))
        return calculateCubicMeters(nodes, edge)
    }

    fun part1(): Long {
        val input = getInput()
        val nodes = mutableListOf<Coordinate>()
        var xIndex = 1L
        var yIndex = 1L
        var edge = 0L
        input.forEach {
            nodes.add(Coordinate(xIndex, yIndex))
            when(it.direction) {
                "R" -> { xIndex += it.length}
                "D" -> { yIndex += it.length}
                "U" -> { yIndex -= it.length}
                else -> { xIndex -= it.length}
            }
            edge += it.length
        }
        nodes.add(Coordinate(xIndex, yIndex))
        return calculateCubicMeters(nodes, edge)
    }

    fun getInput(): List<Entry> {
        val input = Util.getLinesFromFile("/day18/input.txt")
        return input.map {
           val splits = it.split(" ")
           Entry(
               splits[0],
               splits[1].toLong(),
               splits[2].replace(bracketRegex, "")
           )
        }
    }

    fun getInput2(): List<Entry> {
        val input = Util.getLinesFromFile("/day18/input.txt")
        return input.map {
            val splits = it.split(" ")
            val hexString = splits[2].replace(bracketRegex, "")
            val direction = hexString.last().toString()
            val length = hexString.substring(0, hexString.length - 1).replace("#", "").toLong(radix = 16)
            Entry(
                direction,
                length,
                hexString
            )
        }
    }
}

fun main() {
    val day18 = Day18()
    var resultPart1: Long
    var resultPart2: Long
    val timePart1 = measureTimeMillis {
        resultPart1 = day18.part1()
    }
    val timePart2 = measureTimeMillis {
        resultPart2 = day18.part2()
    }
    println("Part1(${timePart1}ms): " + resultPart1)
    println("=================")
    println("Part2(${timePart2}ms): " + resultPart2)
}