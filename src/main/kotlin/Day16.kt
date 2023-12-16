package main.kotlin

class Day16 {
    data class Movement(val x: Int, val y: Int)
    data class Coordinate(val x: Int, val y: Int)

    val globalTileMap = mutableMapOf<Pair<Coordinate, Movement>, Int>()
    var temporaryGlobalMap = mutableMapOf<Pair<Coordinate, Movement>, Int>()

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
        contraptionLayout: List<String>,
        visitedTileMap: MutableMap<Pair<Coordinate, Movement>, Int> = globalTileMap
    ): Int {
        visitedTileMap[Pair(currentLocation, movement)] =
            visitedTileMap.getOrDefault(Pair(currentLocation, movement), 0) + 1
        if (visitedTileMap[Pair(currentLocation, movement)]!! > 1) return 1
        val nextCoordinate = currentLocation.update(movement)
        //is Valid Location?
        return if (nextCoordinate.isValid(contraptionLayout)) {
            val movements = contraptionLayout[nextCoordinate.y][nextCoordinate.x].alterMovement(movement)
            if (movements.size == 2) {
                takeStep(nextCoordinate, movements.last(), contraptionLayout, visitedTileMap) +
                        takeStep(nextCoordinate, movements.first(), contraptionLayout, visitedTileMap)
            } else {
                takeStep(nextCoordinate, movements.first(), contraptionLayout, visitedTileMap)
            }
        } else {
            0
        }
    }

    fun MutableMap<Pair<Coordinate, Movement>, Int>.printTheMap(input: List<String>) {
        val map = input.map { ".".repeat(it.length) }.toMutableList()
        this.entries.forEach { (coor, count) ->
            map[coor.first.y] = map[coor.first.y].replaceRange(coor.first.x, coor.first.x + 1, "$count")
        }
        map.forEach { println(it) }
    }

    val MutableMap<Pair<Coordinate, Movement>, Int>.score: Long
        get() {
            return this.keys.groupBy { it.first }.count().toLong()
        }

    fun doTheThing(): Long {
        val input = getInput()
        try {
            val result = input[0][0].alterMovement(Movement(1, 0)).sumOf {
                takeStep(Coordinate(0, 0), it, input)
            }
            println(result)
        } catch (error: StackOverflowError) {
            globalTileMap.printTheMap(input)
            println(globalTileMap.score)
            System.exit(-10)
        }
        globalTileMap.printTheMap(input)
        return globalTileMap.score
    }

    fun doTheOtherThing(): Long {
        val input = getInput()
        var max = -1L
        try {
            input.forEachIndexed { indexY, entry ->
                listOf(0, entry.length - 1).forEach { startX ->
                    val movX = if (startX == 0) 1 else -1
                    input[indexY][startX].alterMovement(Movement(movX, 0)).forEach {
                        temporaryGlobalMap = mutableMapOf()
                        takeStep(Coordinate(indexY, startX), it, input, temporaryGlobalMap)
                        if (temporaryGlobalMap.score > max) max = temporaryGlobalMap.score
                    }
                }
            }
            input[0].forEachIndexed { indexX, str ->
                listOf(0, input.size - 1).forEach { startY ->
                    val movY = if (startY == 0) 1 else -1
                    input[startY][indexX].alterMovement(Movement(0, movY)).forEach {
                        temporaryGlobalMap = mutableMapOf()
                        takeStep(Coordinate(startY, indexX), it, input, temporaryGlobalMap)
                        if (temporaryGlobalMap.score > max) max = temporaryGlobalMap.score
                    }
                }
            }
        } catch (error: StackOverflowError) {
            temporaryGlobalMap.printTheMap(input)
            println(temporaryGlobalMap.score)
            println(max)
            System.exit(-10)
        }
        temporaryGlobalMap.printTheMap(input)
        return max
    }
}

fun main() {
    val asdf = Day16()
    println(asdf.doTheThing())
    println("===========================================================================================")
    println(asdf.doTheOtherThing())
}