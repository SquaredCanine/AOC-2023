package main.kotlin

class Day16 {
    data class Movement(val x: Int, val y: Int)
    data class Coordinate(val x: Int, val y: Int)

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

    fun figureItOut(
        currentLocation: Coordinate,
        movement: Movement,
        contraptionLayout: List<String>
    ): Int {
        val tiles = Array(contraptionLayout.size) { contraptionLayout[0].map { false }.toMutableList() }
        val seen = mutableSetOf<WomboCombo>()
        var next = listOf(Pair(currentLocation, movement))
        while (next.isNotEmpty()) {
            next = next.mapNotNull { entry ->
                val (coordinates, movement) = entry
                if (!coordinates.isValid(contraptionLayout) || seen.contains(entry)) {
                    null
                } else {
                    tiles[coordinates.y][coordinates.x] = true
                    seen.add(entry)
                    contraptionLayout[coordinates.y][coordinates.x].alterMovement(movement).map { nextMove ->
                        Pair(coordinates.update(nextMove), nextMove)
                    }
                }
            }.flatten()
        }
        return tiles.sumOf { row -> row.count { it } }
    }

    val MutableMap<Pair<Coordinate, Movement>, Int>.score: Long
        get() {
            return this.keys.groupBy { it.first }.count().toLong()
        }

    fun doTheThing(): Int {
        val input = getInput()
        return figureItOut(Coordinate(0, 0), Movement(1, 0), input)
    }

    fun doTheOtherThing(): Int {
        val input = getInput()
        var max = -1
        input.forEachIndexed { indexY, entry ->
            listOf(0, entry.length - 1).forEach { startX ->
                val movX = if (startX == 0) 1 else -1
                val result = figureItOut(Coordinate(startX, indexY), Movement(movX, 0), input)
                if (result > max) max = result

            }
        }
        input[0].forEachIndexed { indexX, str ->
            listOf(0, input.size - 1).forEach { startY ->
                val movY = if (startY == 0) 1 else -1
                val result = figureItOut(Coordinate(indexX, startY), Movement(0, movY), input)
                if (result > max) max = result
            }
        }
        return max
    }
}

typealias WomboCombo = Pair<Day16.Coordinate, Day16.Movement>

fun main() {
    val asdf = Day16()
    println(asdf.doTheThing())
    println(asdf.doTheOtherThing())
}