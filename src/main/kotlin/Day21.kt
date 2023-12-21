package main.kotlin

import main.kotlin.Util.Companion.isValid
import main.kotlin.Util.Coordinate

class Day21 {

    fun getInput(): Pair<List<List<Char>>, Coordinate> {
        val input = Util.getLinesFromFile("/day21/input.txt")
        var start = Coordinate(0, 0)
        return Pair(input.mapIndexed { yIndex, it ->
            it.mapIndexed { xIndex, it ->
                if (it == 'S') start = Coordinate(xIndex, yIndex)
                it
            }
        }, start)
    }

    fun part1(): Long {
        val (input, start) = getInput()
        val reachAbleEven: MutableMap<Coordinate, Boolean> = mutableMapOf()
        val amountOfSteps = 64
        var steps: List<Coordinate> = listOf(start)
        for (i in 1..amountOfSteps) {
            var nextSteps: MutableList<Coordinate> = mutableListOf()
            steps.forEach {
                nextSteps.addAll(
                    listOf(
                        Coordinate(it.x + 1, it.y),
                        Coordinate(it.x - 1, it.y),
                        Coordinate(it.x, it.y + 1),
                        Coordinate(it.x, it.y - 1),
                    )
                )
            }
            nextSteps = nextSteps
                .filter { it.isValid(input) && input[it.y][it.x] != '#' && reachAbleEven[it] == null }
                .also {
                    it.forEach {
                        val even = i % 2 == 0
                        reachAbleEven[it] = even
                    }
                }
                .distinct()
                .toMutableList()
            steps = nextSteps
        }
        input.forEachIndexed { yIndex, it ->
            it.forEachIndexed { xIndex, it ->
                val res = reachAbleEven[Coordinate(xIndex, yIndex)]
                if (res == null || res == (amountOfSteps % 2 != 0)) print(it) else print('O')
            }
            println()
        }
        return reachAbleEven.values.count { it == (amountOfSteps % 2 == 0) }.toLong()
    }

    fun part2(): Long {
        val (input, start) = getInput()
        val reachAbleEven: MutableMap<Coordinate, Boolean> = mutableMapOf()

        val gridLength = input[0].size.toLong()
        val totalSteps = 26501365

        val grids: Long = totalSteps / gridLength // 202300

        val rem: Long = totalSteps % gridLength // 65

        var results = listOf<Int>()

        var stepList: List<Coordinate> = listOf(start)

        var steps = 0

        val allowedPlots = mutableSetOf<Coordinate>()
        input.forEachIndexed { yIndex, it ->
            it.forEachIndexed { xIndex, it ->
                if (it == 'S' || it == '.') allowedPlots.add(Coordinate(xIndex, yIndex))
            }
        }

        for (n in 0 until 3) {
            while (steps < n * gridLength + rem) {
                var nextSteps: MutableList<Coordinate> = mutableListOf()
                stepList.forEach {
                    nextSteps.addAll(
                        listOf(
                            Coordinate(it.x + 1, it.y),
                            Coordinate(it.x - 1, it.y),
                            Coordinate(it.x, it.y + 1),
                            Coordinate(it.x, it.y - 1),
                        )
                    )
                }
                nextSteps = nextSteps
                    .mapNotNull { coord ->
                        val newXRem: Int = (coord.x % 131 + 131) % 131
                        val newYRem: Int = (coord.y % 131 + 131) % 131
                        if (allowedPlots.contains(Coordinate(newXRem, newYRem))) {
                            val even = steps % 2 == 0
                            reachAbleEven[Coordinate(coord.x, coord.y)] = even
                            Coordinate(coord.x, coord.y)
                        } else {
                            null
                        }
                    }
                    .distinct()
                    .toMutableList()
                stepList = nextSteps
                steps++
            }
            results += reachAbleEven.values.count { it == (steps % 2 == 1) }
        }
        val F0 = results[0]
        val F1 = results[1]
        val F2 = results[2]
        println(
            """
            F0: $F0
            F1: $F1
            F2: $F2 
        """.trimIndent()
        )
        val oneAoneB = F1 - F0
        val fourAtwoB = F2 - F0
        val twoAtwoB = 2 * oneAoneB
        val a = (fourAtwoB - twoAtwoB) / 2
        val b = oneAoneB - a
        return a * (grids * grids) + b * grids + F0;
    }
}

fun main() {
    println(Day21().part1())
    println(Day21().part2())
}

//F(0) = 3858
//F(1) =
//F(2) =