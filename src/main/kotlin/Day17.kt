package main.kotlin

import main.kotlin.Util.Companion.isValid
import main.kotlin.Util.Coordinate
import kotlin.math.abs

class Day17 {

    fun NodeA.getAvailableMoves(): List<Coordinate> {
        val coordinates = mutableListOf<Coordinate>()
        if (this.parent != null) {
            val xMovement = this.coordinate.x - this.parent.coordinate.x
            // Left, Right, Up or Down
            if (xMovement != 0) {
                coordinates.addAll(
                    listOf(
                        Coordinate(this.coordinate.x, this.coordinate.y + 1),
                        Coordinate(this.coordinate.x, this.coordinate.y - 1)
                    )
                )
            } else {
                coordinates.addAll(
                    listOf(
                        Coordinate(this.coordinate.x + 1, this.coordinate.y),
                        Coordinate(this.coordinate.x - 1, this.coordinate.y)
                    )
                )
            }
            // Can move forward?
            val threeParentsBack = this.parent.parent?.parent
            if (threeParentsBack != null) {
                val wayBackWhen = threeParentsBack.coordinate
                val translatedCoordinate = Coordinate(
                    abs(this.coordinate.x - wayBackWhen.x),
                    abs(this.coordinate.y - wayBackWhen.y)
                )
                if (translatedCoordinate.x != 3 && translatedCoordinate.y != 3) {
                    if (xMovement != 0) {
                        coordinates.add(Coordinate(this.coordinate.x + xMovement, this.coordinate.y))
                    } else {
                        val yMovement = this.coordinate.y - this.parent.coordinate.y
                        coordinates.add(Coordinate(this.coordinate.x, this.coordinate.y + yMovement))
                    }
                }
            } else {
                if (xMovement != 0) {
                    coordinates.add(Coordinate(this.coordinate.x + xMovement, this.coordinate.y))
                } else {
                    val yMovement = this.coordinate.y - this.parent.coordinate.y
                    coordinates.add(Coordinate(this.coordinate.x, this.coordinate.y + yMovement))
                }
            }
        } else {
            coordinates.addAll(
                listOf(
                    Coordinate(0, 1),
                    Coordinate(1, 0)
                )
            )
        }
        return coordinates
    }

    val NodeA.direction: Int
        get() =
            if (this.parent != null) {
                val xMovement = this.coordinate.x - this.parent.coordinate.x
                val yMovement = this.coordinate.y - this.parent.coordinate.y
                if (xMovement != 0) {
                    if (xMovement == -1) {
                        0
                    } else {
                        1
                    }
                } else {
                    if (yMovement == -1) {
                        2
                    } else {
                        3
                    }
                }
            } else {
                -1
            }


    val NodeA.runLength: Int
        get() = if (this.parent != null) {
            val xMovement = this.coordinate.x - this.parent.coordinate.x
            if (this.parent.parent != null) {
                val xMovement2 = this.coordinate.x - this.parent.coordinate.x
                if (xMovement != 0 && xMovement2 != 0) {
                    2
                } else if (xMovement == 0 && xMovement2 == 0) {
                    2
                } else {
                    1
                }
            } else {
                1
            }
        } else {
            0
        }

    fun NodeD.getAvailableMoves(): List<Coordinate> {
        val coordinates = mutableListOf<Coordinate>()
        if (this.parent != null) {
            val xMovement = this.coordinate.x - this.parent.coordinate.x
            // Left, Right, Up or Down
            if (xMovement != 0) {
                coordinates.addAll(
                    listOf(
                        Coordinate(this.coordinate.x, this.coordinate.y + 1),
                        Coordinate(this.coordinate.x, this.coordinate.y - 1)
                    )
                )
            } else {
                coordinates.addAll(
                    listOf(
                        Coordinate(this.coordinate.x + 1, this.coordinate.y),
                        Coordinate(this.coordinate.x - 1, this.coordinate.y)
                    )
                )
            }
            // Can move forward?
            val threeParentsBack = this.parent.parent?.parent
            if (threeParentsBack != null) {
                val wayBackWhen = threeParentsBack.coordinate
                val translatedCoordinate = Coordinate(
                    abs(this.coordinate.x - wayBackWhen.x),
                    abs(this.coordinate.y - wayBackWhen.y)
                )
                if (translatedCoordinate.x != 3 && translatedCoordinate.y != 3) {
                    if (xMovement != 0) {
                        coordinates.add(Coordinate(this.coordinate.x + xMovement, this.coordinate.y))
                    } else {
                        val yMovement = this.coordinate.y - this.parent.coordinate.y
                        coordinates.add(Coordinate(this.coordinate.x, this.coordinate.y + yMovement))
                    }
                }
            } else {
                if (xMovement != 0) {
                    coordinates.add(Coordinate(this.coordinate.x + xMovement, this.coordinate.y))
                } else {
                    val yMovement = this.coordinate.y - this.parent.coordinate.y
                    coordinates.add(Coordinate(this.coordinate.x, this.coordinate.y + yMovement))
                }
            }
        } else {
            coordinates.addAll(
                listOf(
                    Coordinate(0, 1),
                    Coordinate(1, 0)
                )
            )
        }
        return coordinates
    }

    fun calculateHeuristic(validCoordinate: Coordinate, map: List<List<Int>>): Int {
//        val x = validCoordinate.x
//        val y = validCoordinate.y
//        return (map.size - 1 - validCoordinate.y) +
//                (map[0].size - 1 - validCoordinate.x) *
//                (listOf(
//                    Coordinate(x + 1, y),
//                    Coordinate(x, y + 1),
//                    Coordinate(x + 1, y + 1),
//                    Coordinate(x + -1, y),
//                    Coordinate(x, y -1),
//                    Coordinate(x - 1, y + 1)
//                ).filter { it.isValid(map) }.minOfOrNull { map[it.y][it.x] } ?: 1)
        return 0
    }

    data class NodeA(val coordinate: Coordinate, val parent: NodeA?, val f: Int, val g: Int, val h: Int)
    data class NodeD(val coordinate: Coordinate, val parent: NodeD?)


    fun part1(): Int {
        return aStarGarbage()
    }

    fun dijkstaFun(): Int {
        val input = getInput()
        val nodeList = mutableListOf(
            NodeD(Coordinate(0, 0), null)
        )
        val goalCoordinate = Coordinate(input[0].size - 1, input.size - 1)

        return 0
    }

    fun aStarGarbage(): Int {
        val input = getInput()
        val openList = mutableListOf(
            NodeA(Coordinate(0, 0), null, 0, 0, 0)
        )
        val closedList = mutableListOf<NodeA>()
        val goalCoordinate = Coordinate(input[0].size - 1, input.size - 1)
        while (openList.isNotEmpty()) {
            val currentNode = openList.minBy { it.f }
            openList.remove(currentNode)
            closedList.add(currentNode)

            //Goal found
            if (currentNode.coordinate == goalCoordinate) {
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
                return path.sumOf { input[it.coordinate.y][it.coordinate.x] }
            }

            currentNode
                //Get possible moves
                .getAvailableMoves()
                // Filter those for validity
                .filter { newCoordinate ->
                    newCoordinate.isValid(input) && closedList.none { it.coordinate == newCoordinate }
                }
                // Create nodes and calculate f, g and h
                .map { validCoordinate ->
                    val g = currentNode.g + input[validCoordinate.y][validCoordinate.x]
                    val h = calculateHeuristic(validCoordinate, input)
                    NodeA(validCoordinate, currentNode, g + h, g, h)
                }
                // Add them to the pile if new or lowest value
                .forEach {
                    val similarNodes = openList.filter { openNode ->
                        it.coordinate == openNode.coordinate &&
                                it.direction == openNode.direction &&
                                it.runLength == openNode.runLength
                    }
                    if (similarNodes.isEmpty() || it.g < similarNodes.minBy { it.g }.g) {
                        openList.add(it)
                    }
                }
        }
        return -1
    }

    fun part2(): Int {
        return 0
    }

    fun getInput(): List<List<Int>> {
        val input = Util.getLinesFromFile("/day17/test.txt")
        return input.map { it.map { it.digitToInt() } }
    }
}

fun main() {
    println(Day17().part1())
    println(Day17().part2())
}