package main.kotlin

class Day16 {
    data class Movement(val x: Int, val y: Int)
    data class Coordinate(val x: Int, val y: Int)

    val visitedTileMap = mutableMapOf<Pair<Coordinate, Movement>, Int>()

    fun getInput(): List<String> {
        return Util.getLinesFromFile("/day16/input.txt")
    }

    fun Coordinate.update(movement: Movement): Coordinate {
        return Coordinate(
            this.x + movement.x,
            this.y + movement.y
        )
    }

    fun Coordinate.isValid(layout: List<String>): Boolean {
        return this.x in layout[0].indices &&
                this.y in layout.indices
    }

    fun lookAhead(nextCoordinate: Coordinate, movement: Movement, contraptionLayout: List<String>): Int {
        val nextNextCoordinate = nextCoordinate.update(movement)
        return if (nextNextCoordinate.isValid(contraptionLayout)) {
            1
        } else {
            0
        }
    }

    fun Char.alterMovement(movement: Movement): List<Movement> {
        return when (this) {
            '|' -> {
                if (movement.y != 0) {
                    listOf(movement)
                } else {
                    listOf(Movement(0, 1), Movement(0, -1))
                }
            }

            '/' -> {
                if (movement.x != 0) {
                    listOf(Movement(0, movement.x * -1))
                } else {
                    listOf(Movement(movement.y * -1, 0))
                }
            }

            '\\' -> {
                if (movement.x != 0) {
                    listOf(Movement(0, movement.x))
                } else {
                    listOf(Movement(movement.y, 0))
                }
            }

            '-' -> {
                return if (movement.x != 0) {
                    listOf(movement)
                } else {
                    listOf(Movement(1, 0), Movement(-1, 0))
                }
            }

            else -> {
                //Carry on
                listOf(movement)
            }
        }
    }

    fun takeStep(
        currentLocation: Coordinate,
        movement: Movement,
        contraptionLayout: List<String>
    ): Int {
        visitedTileMap[Pair(currentLocation, movement)] =
            visitedTileMap.getOrDefault(Pair(currentLocation, movement), 0) + 1
        if (visitedTileMap[Pair(currentLocation, movement)]!! > 1) return 1
        val nextCoordinate = currentLocation.update(movement)
        //is Valid Location?
        return if (nextCoordinate.isValid(contraptionLayout)) {
            val movements = contraptionLayout[nextCoordinate.y][nextCoordinate.x].alterMovement(movement)
            movements.sumOf {
                takeStep(nextCoordinate, it, contraptionLayout)
            }
        } else {
            0
        }
    }

    fun printTheMap(input: List<String>) {
        val map = input.map { ".".repeat(it.length) }.toMutableList()
        visitedTileMap.entries.forEach { (coor, count) ->
            map[coor.first.y] = map[coor.first.y].replaceRange(coor.first.x, coor.first.x + 1, "$count")
        }
        map.forEach { println(it) }
    }

    fun doTheThing(): Long {
        val input = getInput()
        try {
            val result = input[0][0].alterMovement(Movement(1, 0)).sumOf {
                takeStep(Coordinate(0, 0), it, input)
            }
            println(result)
        } catch (error: StackOverflowError) {
            printTheMap(input)
            System.exit(0)
        }
        printTheMap(input)
        return visitedTileMap.keys.groupBy { it.first }.count().toLong()
    }

    fun doTheOtherThing(): Long {
        return 0L
    }
}

fun main() {
    println(Day16().doTheThing())
    println(Day16().doTheOtherThing())
}