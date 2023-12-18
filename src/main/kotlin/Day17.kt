package main.kotlin

import main.kotlin.Util.Coordinate
import kotlin.math.abs

class Day17 {

    data class NodeA(val coordinate: Coordinate, val parent: NodeA?, val f: Int, val g: Int, val h: Int)
    data class NodeD(val coordinate: Coordinate, val parent: NodeD?)

    fun Coordinate.getPreviousSteps(map: MutableMap<Coordinate, Coordinate?>): List<Coordinate> {
        val coordinates = mutableListOf<Coordinate>()
        var current = this
        (0..2).forEach {
            val new = map[current]
            if (new != null) {
                current = new
                coordinates.add(current)
            }
        }
        return coordinates
    }

    fun List<Coordinate>.getNextCoordinates(queue: List<Coordinate>, current: Coordinate): List<Coordinate> {
        val coordinateList = mutableListOf<Coordinate>()
        if (this.isEmpty()) {
            coordinateList.addAll(
                listOf(
                    Coordinate(0, 1),
                    Coordinate(1, 0)
                )
            )
        } else {
            val xMovement = current.x - this.first().x
            val yMovement = current.y - this.first().y

            if (xMovement != 0) {
                coordinateList.addAll(
                    listOf(
                        Coordinate(current.x, current.y + 1),
                        Coordinate(current.x, current.y - 1)
                    )
                )
            } else {
                coordinateList.addAll(
                    listOf(
                        Coordinate(current.x + 1, current.y),
                        Coordinate(current.x - 1, current.y)
                    )
                )
            }

            if (this.size >= 2) {
                val diagonalCoord = Coordinate(
                    current.x - this[1].x,
                    current.y - this[1].y
                )
                if (abs(diagonalCoord.x) == 1 && abs(diagonalCoord.y) == 1) {
                    println("Diagonal move detected!")
                    val possibilities = listOf(
                        Coordinate(
                            this[1].x + diagonalCoord.x,
                            this[1].y
                        ),
                        Coordinate(
                            this[1].x,
                            this[1].y + diagonalCoord.y
                        )
                    )
                    println(possibilities)
                    coordinateList.removeAll(possibilities)
                }
            }

            // Can move forward?
            val wayBackWhen = this.last()
            val translatedCoordinate = Coordinate(
                abs(current.x - wayBackWhen.x),
                abs(current.y - wayBackWhen.y)
            )
            if (translatedCoordinate.x != 3 && translatedCoordinate.y != 3) {
                if (xMovement != 0) {
                    coordinateList.add(Coordinate(current.x + xMovement, current.y))
                } else {
                    coordinateList.add(Coordinate(current.x, current.y + yMovement))
                }
            }

        }
        return coordinateList.filter { queue.contains(it) }
    }

    fun part1(): Int {
        val input = getInput()
        val queue = input.mapIndexed { yIndex, numbers ->
            numbers.mapIndexed { xIndex, number ->
                Coordinate(xIndex, yIndex)
            }
        }.flatten().toMutableList()

        val distance = queue.associateWith { coor ->
            Integer.MAX_VALUE
        }.toMutableMap()

        val previous: MutableMap<Coordinate, Coordinate?> = queue.associateWith { coor -> null }.toMutableMap()
        val startPoint = Coordinate(0, 0)
        val stopPoint = Coordinate(input[0].size - 1, input.size - 1)
        distance[startPoint] = 0
        while (queue.isNotEmpty()) {
            val current = queue.minBy { distance[it] ?: Integer.MAX_VALUE }
            queue.remove(current)
            if (current == stopPoint) {
                var nextNode: Coordinate? = current
                var sum = 0
                val outputPrint = Array(input.size) { y -> Array(input[0].size) { x -> input[y][x].toString() } }
                while (nextNode != null) {
                    sum += input[nextNode.y][nextNode.x]
                    outputPrint[nextNode.y][nextNode.x] = "#"
                    nextNode = previous[nextNode]
                }
                outputPrint.forEach { println(it.joinToString("")) }
                return sum
            }
            current
                .getPreviousSteps(previous)
                .getNextCoordinates(queue, current)
                .forEach { neigh ->
                    val alt = distance[current]!! + input[neigh.y][neigh.x]
                    if (alt < distance[neigh]!!) {
                        distance[neigh] = alt
                        previous[neigh] = current
                    }
                }
        }
        return -1
    }

    fun part2(): Int {
        return 0
    }

    fun getInput(): List<List<Int>> {
        val input = Util.getLinesFromFile("/day17/input.txt")
        return input.map { it.map { it.digitToInt() } }
    }
}

fun main() {
    println(Day17().part1())
    println(Day17().part2())
}