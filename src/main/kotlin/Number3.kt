package main.kotlin

class Number3 {
    val gearLocations = mutableListOf<Pair<Int, Int>>()
    val gearMap = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()

    fun parseNumberIfValid(array: List<String>, startNumber: Pair<Int, Int>, endNumber: Pair<Int, Int>): Int? {
        val rowIndices = ((startNumber.second - 1)..(endNumber.second + 1)).toList()
        val searchArray = ((startNumber.first - 1)..(startNumber.first + 1))
            .map { lineIndex ->
                rowIndices.map {
                    Pair(lineIndex, it)
                }
            }
            .flatten()
            .filter {
                it.first >= 0 &&
                        it.second >= 0 &&
                        it.first < array.size &&
                        it.second < array[0].length
            }
        var gearFound = false
        var gearLocation: Pair<Int, Int>? = null;
        val isValid = searchArray.any { (line, character) ->
            val char = array[line][character]
            if (char == '*') {gearFound = true; gearLocation = Pair(line, character)}
            (!char.isDigit() && char != '.')
        }
//        //DEBUG
//        var lineIndex = -1
//        println("Valid number? ${isValid}")
//        searchArray.forEach {
//            if (lineIndex != it.first) {
//                println()
//                lineIndex = it.first
//            }
//            print(array[it.first][it.second])
//        }
//        println()
//        println()
        return if (isValid) {
            val parsedNumber = array[startNumber.first].substring(startNumber.second..endNumber.second).toInt()
            if (gearFound) {
                if (gearMap.containsKey(gearLocation)) {
                    gearMap[gearLocation]!!.add(parsedNumber)
                } else {
                    gearMap[gearLocation!!] = mutableListOf(parsedNumber)
                }
            }
            parsedNumber
        } else {
            null
        }
    }

    fun doTheThing(): Int {
        val inputs = number1::class.java.getResourceAsStream("/day3/input.txt").bufferedReader().readLines()
        val numberList = mutableListOf<Int>()
        var startNumber: Pair<Int, Int>? = null
        inputs.forEachIndexed { lineIndex, line ->
            line.forEachIndexed { characterIndex, char ->
                if (startNumber == null && char.isDigit()) {
                    startNumber = Pair(lineIndex, characterIndex)
                }
                val keepParsing = (characterIndex+1) < line.length && (line[characterIndex+1].isDigit())
                if (startNumber != null && !keepParsing) {
                    val result = parseNumberIfValid(inputs, startNumber!!, Pair(lineIndex, characterIndex))
                    if (result != null) numberList.add(result)
                    startNumber = null
                }
            }
        }
        println(gearMap.values.filter { it.size == 2 }.map { it[0] * it[1] }.sum())
        return numberList.sum()
    }
}

fun main() {
    println(Number3().doTheThing())
}