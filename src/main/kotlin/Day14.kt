package main.kotlin

import main.kotlin.Util.Companion.transpose

class Day14 {

    fun getTheInput(): List<String> {
        val inputs = Util.getLinesFromFile("/day14/input.txt")
        return inputs
    }

    fun List<String>.calculateLoad(): Int {
        return this.reversed().mapIndexed { int, string ->
            (int + 1) * string.count { it == 'O' }
        }.sum()
    }

    fun List<String>.moveTheRocks(): List<String> {
        val tilted = this.toMutableList()
        this.getRockLocations().forEach { (x, y) ->
            val thingsAhead = tilted[y].substring(0, x + 1)
            val solidRock = thingsAhead.indexOfLast { it == '#' }
            val newLocationIndex = if (solidRock == -1) {
                //Move rock to first empty spot
                thingsAhead.indexOfFirst { it == '.' }
            } else {
                val location = thingsAhead.substring(solidRock, x + 1).indexOfFirst { it == '.' }
                if (location == -1) {
                    location
                } else {
                    location + solidRock
                }
            }
            if (newLocationIndex > -1) {
                tilted[y] = tilted[y]
                    // Replace the rock
                    .replaceRange(x, x + 1, ".")
                    // Move! That! Rock!
                    .replaceRange(newLocationIndex, newLocationIndex + 1, "O")
            }
        }
        return tilted
    }

    fun List<String>.getRockLocations(): List<Pair<Int, Int>> {
        return this.mapIndexed { yIndex, str ->
            val results = str.mapIndexed { xIndex, char ->
                if (char == 'O') {
                    Pair(xIndex, yIndex)
                } else {
                    null
                }
            }
            results
        }
            .flatten()
            .filterNotNull()
    }

    val debugRotation = false

    val northMap = mutableMapOf<String, List<String>>()
    val westMap = mutableMapOf<String, List<String>>()
    val southMap = mutableMapOf<String, List<String>>()
    val eastMap = mutableMapOf<String, List<String>>()
    val fullMap = mutableMapOf<String, List<String>>()
    val indexMap = mutableMapOf<String, Int>()


    fun doTheOtherThing(): Int {
        val inputs = getTheInput()
        var currentTilt = inputs.transpose()
        var didIt = false
        var i = 0
        while (i in 0 until 1_000_000_000) {
            currentTilt = fullMap.getOrPut(currentTilt.joinToString("")) {
                //North
                currentTilt = northMap.getOrPut(currentTilt.joinToString("")) {
                    currentTilt.moveTheRocks()
                }
                if (debugRotation) {
                    println("North")
                    currentTilt.transpose().forEach { println(it) }
                }
                //West
                currentTilt = currentTilt.transpose()
                currentTilt = westMap.getOrPut(currentTilt.joinToString("")) {
                    currentTilt.moveTheRocks()
                }
                if (debugRotation) {
                    println("West")
                    currentTilt.forEach { println(it) }
                }
                //South
                currentTilt = currentTilt.reversed().transpose()
                currentTilt = southMap.getOrPut(currentTilt.joinToString("")) {
                    currentTilt.moveTheRocks()
                }
                if (debugRotation) {
                    println("South")
                    currentTilt.transpose().reversed().forEach { println(it) }
                }
                //East
                currentTilt = currentTilt.transpose().reversed().map { it.reversed() }
                currentTilt = eastMap.getOrPut(currentTilt.joinToString("")) {
                    currentTilt.moveTheRocks()
                }
                if (debugRotation) {
                    println("East")
                    currentTilt.map { it.reversed() }.forEach { println(it) }
                }
                // Reset to North
                currentTilt = currentTilt.map { it.reversed() }.transpose()
                currentTilt
            }
            val test = indexMap[currentTilt.joinToString("")]
            if (test != null && !didIt) {
                val loopLength = i - test
                val remaining = 1_000_000_000 - 1 - i
                val remainingMod = remaining % loopLength
                println("Massive skip time! $i ->  ${1_000_000_000 - remainingMod}")
                i = 1_000_000_000 - remainingMod
                didIt = true
            } else {
                indexMap[currentTilt.joinToString("")] = i
                i++
            }
        }
        return currentTilt.transpose().calculateLoad()
    }

    fun doTheThing(): Int {
        val inputs = getTheInput()
        val result = inputs.transpose().moveTheRocks()
        return result.transpose().calculateLoad()
    }

}

fun main() {
    println(Day14().doTheThing())
    println(Day14().doTheOtherThing())
}