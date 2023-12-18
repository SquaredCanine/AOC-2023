package main.kotlin

import main.kotlin.Util.Companion.isValid
import main.kotlin.Util.Coordinate
import java.util.*
import kotlin.math.abs

class Day17 {

    fun NodeA.getDirection(nextCoordinate: Coordinate): Int {
        val movement = Coordinate(
            this.coordinate.x - nextCoordinate.x,
            this.coordinate.y - nextCoordinate.y
        )
        return when {
            movement.x == -1 -> {
                0
            }

            movement.x == 1 -> {
                1
            }

            movement.y == -1 -> {
                2
            }

            else -> {
                3
            }
        }
    }

    fun NodeA.getAvailableMovesUltra(): List<Coordinate> {
        if (this.direction == -1) {
            return listOf(
                Coordinate(0, 1),
                Coordinate(1, 0)
            )
        }
        val (x, y) = this.coordinate
        val coordinates = mutableListOf<Coordinate>()
        if (this.runLength <= 8) {
            coordinates.add(when (this.direction) {
                0 -> { Coordinate(x + 1, y)}
                1 -> { Coordinate(x - 1, y)}
                2 -> { Coordinate(x, y + 1)}
                else -> { Coordinate(x, y - 1)}
            })
        }
        if (this.runLength > 2) {
            when (this.direction) {
                0, 1 -> {
                    coordinates.addAll(
                        listOf(
                            Coordinate(x, y + 1),
                            Coordinate(x, y - 1)
                        )
                    )
                }

                else -> {
                    coordinates.addAll(
                        listOf(
                            Coordinate(x + 1, y),
                            Coordinate(x - 1, y)
                        )
                    )
                }
            }
        }
        return coordinates
    }

    fun NodeA.getAvailableMovesBasic(): List<Coordinate> {
        val (x, y) = this.coordinate
        val coordinates = mutableListOf<Coordinate>()
        when(this.direction) {
            -1 -> {
                return listOf(
                    Coordinate(0, 1),
                    Coordinate(1, 0)
                )
            }
            0, 1 -> {
                coordinates.addAll(
                    listOf(
                        Coordinate(x, y + 1),
                        Coordinate(x, y - 1)
                    )
                )
            }
            else -> {
                coordinates.addAll(
                    listOf(
                        Coordinate(x + 1, y),
                        Coordinate(x - 1, y)
                    )
                )
            }
        }
        if (this.runLength != 2) {
            coordinates.add(when (this.direction) {
                0 -> { Coordinate(x + 1, y)}
                1 -> { Coordinate(x - 1, y)}
                2 -> { Coordinate(x, y + 1)}
                else -> { Coordinate(x, y - 1)}
            })
        }
        return coordinates
    }

    fun calculateHeuristic(validCoordinate: Coordinate, map: List<List<Int>>): Int {
        return ((map.size - 1) - validCoordinate.y)  +
                (map[0].size - 1) - validCoordinate.x
    }

    data class NodeA(
        val coordinate: Coordinate,
        val parent: NodeA?,
        val f: Int, val g: Int, val h: Int,
        val direction: Int,
        val runLength: Int
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other?.javaClass != javaClass) return false
            other as NodeA
            return this.coordinate == other.coordinate &&
                    this.direction == other.direction &&
                    this.runLength == other.runLength
        }


    }

    fun aStarGarbage(moveSelector: NodeA.() -> List<Coordinate>, stopCondition: Coordinate.(NodeA) -> Boolean): Int {
        val seenBefore: MutableMap<String, Int> = mutableMapOf()
        val input = getInput()
        val openQueue = PriorityQueue<NodeA> { node1, node2 ->
            node1.f - node2.f
        }

        openQueue.offer(
            NodeA(Coordinate(0, 0), null, 0, 0, 0, -1, -1)
        )
        val closedList = mutableListOf<NodeA>()
        val goalCoordinate = Coordinate(input[0].size - 1, input.size - 1)
        var iterations = 0
        while (openQueue.isNotEmpty()) {
            if (iterations % 100_000 == 0) {
                println("iterations $iterations")
                val newThing = Array(input.size) { y -> Array(input[0].size) { "" } }
                closedList.forEach { newThing[it.coordinate.y][it.coordinate.x] = "X" }
                newThing.forEach {
                    val stringToPrint = it.joinToString("")
                    if (stringToPrint.isNotBlank()) {
                        println(stringToPrint)
                    }
                }
            }
            val currentNode = openQueue.poll()
            closedList.add(currentNode)

            //Goal found
            if (goalCoordinate.stopCondition(currentNode)) {
                val path = mutableListOf<NodeA>()
                var current: NodeA? = currentNode
                while (current != null) {
                    path.add(current)
                    current = current.parent
                }
                path.removeLast()
                val newThing = Array(input.size) { y -> Array(input[0].size) { x -> input[y][x].toString() } }
                path.forEach { newThing[it.coordinate.y][it.coordinate.x] = "#" }
                newThing.forEach { println(it.joinToString("")) }
                println("iterations $iterations")
                return path.sumOf { input[it.coordinate.y][it.coordinate.x] }
            }

            currentNode
                //Get possible moves
                .moveSelector()
                // Filter those for validity
                .filter { newCoordinate ->
                    newCoordinate.isValid(input)
                }
                // Create nodes and calculate f, g and h
                .mapNotNull { validCoordinate ->
                    val direction = currentNode.getDirection(validCoordinate)
                    val runLength = if (direction == currentNode.direction) currentNode.runLength + 1 else 0
                    val key = "$validCoordinate$direction$runLength"
                    if (seenBefore[key] == null) {
                        seenBefore[key] = 0
                        val g = currentNode.g + input[validCoordinate.y][validCoordinate.x]
                        val h = calculateHeuristic(validCoordinate, input)
                        NodeA(validCoordinate, currentNode, g + h, g, h, direction, runLength)
                    } else {
                        seenBefore[key] = seenBefore[key]!! + 1
                        null
                    }
                }
                // Add them to the pile if new or lowest value
                .forEach {
                    iterations++
                    val similarNodes = openQueue.filter { openNode ->
                        it.coordinate == openNode.coordinate &&
                                it.direction == openNode.direction &&
                                it.runLength == openNode.runLength
                    }
                    if (similarNodes.isEmpty() || it.g < similarNodes.minBy { it.g }.g) {
                        openQueue.add(it)
                    }
                }
        }
        return -1
    }

    fun part1(): Int {
        return aStarGarbage({ getAvailableMovesBasic()}) {
            this == it.coordinate
        }
    }

    fun part2(): Int {
        return aStarGarbage({ getAvailableMovesUltra()}) {
             it.runLength > 2 && this == it.coordinate
        }
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