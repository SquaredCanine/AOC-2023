package main.kotlin

class Day8 {
    val instructionRegex = Regex("[LR]+")
    val nodeRegex = Regex("[A-Z1-9][A-Z1-9][A-Z1-9]")

    fun doTheThing(): Int {
        val inputs = Day8::class.java.getResourceAsStream("/day8/input.txt").bufferedReader().readLines()
        var sequence = ""
        val nodes = mutableMapOf<String, Pair<String, String>>()
        var currentNode = "AAA"
        inputs.forEachIndexed { index, line ->
            if (index == 0) {
                sequence = instructionRegex.find(line)!!.value
                println(sequence)
            } else if (index > 1) {
                val initialSplit = line.split("=")
                val nodeKey = nodeRegex.find(initialSplit[0])!!.value
                if (currentNode.isEmpty()) currentNode = nodeKey
                val destinations = nodeRegex.findAll(initialSplit[1]).map { it.groupValues }.flatten().toList()
                nodes[nodeKey] = Pair(destinations[0], destinations[1])
        }}
        var reachedDestination = false
        var stepCounter = 0

        while (!reachedDestination) {
            run loop@{
                sequence.forEach { nextStep ->
                    stepCounter++
                    val nextNodes = nodes[currentNode]!!
                    currentNode = if (nextStep == 'L') {
                        nextNodes.first
                    } else {
                        nextNodes.second
                    }
                    reachedDestination = currentNode == "ZZZ"
                    if (reachedDestination) {
                        return@loop
                    }
                }
            }
        }
        return stepCounter
    }

    fun doTheOtherThing(): Long {
        val inputs = Day8::class.java.getResourceAsStream("/day8/input.txt").bufferedReader().readLines()
        var sequence = ""
        val nodes = mutableMapOf<String, Pair<String, String>>()
        var currentNodes = mutableListOf<String>()
        inputs.forEachIndexed { index, line ->
            if (index == 0) {
                sequence = instructionRegex.find(line)!!.value
                println(sequence)
            } else if (index > 1) {
                val initialSplit = line.split("=")
                val nodeKey = nodeRegex.find(initialSplit[0])!!.value
                if (nodeKey.endsWith("A")) currentNodes.add(nodeKey)
                val destinations = nodeRegex.findAll(initialSplit[1]).map { it.groupValues }.flatten().toList()
                nodes[nodeKey] = Pair(destinations[0], destinations[1])
            }}
        var reachedDestination = false
        var stepCounter = 0
        val stepsNecessary: MutableList<Long> = mutableListOf(0,0,0,0,0,0)
        var notPrinted = true
        while (!reachedDestination) {
            run loop@{
                sequence.forEach { nextStep ->
                    stepCounter++
                    val newNodes = currentNodes.mapIndexed { index, it ->
                        val nextNodes = nodes[it]!!
                        if (nextStep == 'L') {
                            if (nextNodes.first.endsWith('Z')) stepsNecessary[index] = stepCounter.toLong()
                            nextNodes.first
                        } else {
                            if (nextNodes.second.endsWith('Z')) stepsNecessary[index] = stepCounter.toLong()
                            nextNodes.second
                        }
                    }.toMutableList()
                    if (stepsNecessary.all { it > 0 } && notPrinted) {
                        println(stepsNecessary);
                        return calculateIt(stepsNecessary)
                    }
                    reachedDestination = newNodes.all { it.endsWith("Z") }
                    if (reachedDestination) {
                        return@loop
                    } else {
                        currentNodes = newNodes
                    }
                }
            }
        }
        return stepCounter.toLong()
    }
}

fun calculateIt(numbers: List<Long>): Long {
    val largestNumber = numbers.max()
    var factor = largestNumber
    while(true) {
        if (numbers.all { (largestNumber * factor) % it == 0L }) {
            println("$factor $largestNumber")
            return factor * largestNumber
        }

        factor++
    }
}

fun main() {
    println(calculateIt(listOf(19631,13771,21389,17287,23147,20803)))
    println(Day8().doTheOtherThing())
}