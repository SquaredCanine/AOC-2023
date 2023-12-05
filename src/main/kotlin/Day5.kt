package main.kotlin

import kotlin.concurrent.thread
import kotlin.coroutines.coroutineContext

class Day5 {

    data class AlmanacEntry(val rangePair: Pair<Long, Long>, val converter: (Long) -> Long)

    val digitRegex = Regex("\\d+")
    val seeds: MutableList<Long> = mutableListOf()
    val mapList: List<MutableList<AlmanacEntry>> = listOf(
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf()
    )

    fun doTheThing2(): Long {
        val inputs = number1::class.java.getResourceAsStream("/day5/input.txt").bufferedReader().readLines()
        var mapIndex = -1
        inputs.forEach { line ->
            if (line.contains("seeds")) {
                seeds.addAll(
                    digitRegex.findAll(line).map { match -> match.groupValues.map { it.toLong() } }.flatten()
                        .toMutableList()
                )
            } else {
                if (line.contains("-to-")) {
                    mapIndex++
                } else {
                    val values = digitRegex.findAll(line)
                        .map { it.groupValues }
                        .flatten()
                        .map { it.toLong() }
                        .toList()
                    if (values.size == 3) {
                        val diff = values[0] - values[1]
                        mapList[mapIndex].add(
                            AlmanacEntry(
                                Pair(values[1], values[1] + values[2])
                            ) { l -> l + diff }
                        )

                    }
                }

            }
        }
        val result = seeds.map {
            getResult(it)
        }
        return result.minOf { it.second }
    }

    fun doTheThing(): Long {
        val inputs = number1::class.java.getResourceAsStream("/day5/input.txt").bufferedReader().readLines()
        var mapIndex = -1
        inputs.forEach { line ->
            if (line.contains("seeds")) {
                seeds.addAll(
                    digitRegex.findAll(line).map { match -> match.groupValues.map { it.toLong() } }.flatten()
                        .toMutableList()
                )
            } else {
                if (line.contains("-to-")) {
                    mapIndex++
                } else {
                    val values = digitRegex.findAll(line)
                        .map { it.groupValues }
                        .flatten()
                        .map { it.toLong() }
                        .toList()
                    println(values)
                    if (values.size == 3) {
                        val diff = values[0] - values[1]
                        mapList[mapIndex].add(
                            AlmanacEntry(
                                Pair(values[1], values[1] + values[2])
                            ) { l -> l + diff }
                        )

                    }
                }

            }
        }
        val result = seeds.map {
            var currentIndex = it
            print("$currentIndex -> ")
            mapList.forEachIndexed { index, map ->
                currentIndex = getNextIndex(currentIndex, map)
                print("$currentIndex -> ")
            }
            println("<>")
            Pair(it, currentIndex)
        }
        return result.minOf { it.second }
    }

    fun doTheOtherThingINVERSE(): Long {
        val inputs = number1::class.java.getResourceAsStream("/day5/input.txt").bufferedReader().readLines()
        var mapIndex = -1
        inputs.forEach { line ->
            if (line.contains("seeds")) {
                seeds.addAll(
                    digitRegex.findAll(line).map { match -> match.groupValues.map { it.toLong() } }.flatten()
                        .toMutableList()
                )
            } else {
                if (line.contains("-to-")) {
                    mapIndex++
                } else {
                    val values = digitRegex.findAll(line)
                        .map { it.groupValues }
                        .flatten()
                        .map { it.toLong() }
                        .toList()
                    if (values.size == 3) {
                        val diff = values[1] - values[0]
                        mapList[mapIndex].add(
                            AlmanacEntry(
                                Pair(values[0], values[0] + values[2])
                            ) { l -> l + diff }
                        )

                    }
                }

            }
        }
        val ranges: MutableList<LongRange> = mutableListOf()

        for (i in 0 until seeds.size step 2) {
            ranges.add(seeds[i] until seeds[i] + seeds[i+1])
        }
        var foundOriginalSeed = false
        var index = 0L
        while(!foundOriginalSeed) {
            if (index % 250000 == 0L) {
                println("Going strong: $index")
            }
            val result = getResultInverse(index)
            if (isOriginalSeed(result, ranges)) {
                return result.first
            }
            index++
        }
        return -1L
    }

    fun isOriginalSeed(result: Pair<Long, Long>, seeds: List<LongRange>): Boolean {
        return seeds.any { it.contains(result.second) }
    }

    fun doTheOtherThing(): Long {
        val inputs = number1::class.java.getResourceAsStream("/day5/input.txt").bufferedReader().readLines()
        var mapIndex = -1
        inputs.forEach { line ->
            if (line.contains("seeds")) {
                seeds.addAll(
                    digitRegex.findAll(line).map { match -> match.groupValues.map { it.toLong() } }.flatten()
                        .toMutableList()
                )
            } else {
                if (line.contains("-to-")) {
                    mapIndex++
                } else {
                    val values = digitRegex.findAll(line)
                        .map { it.groupValues }
                        .flatten()
                        .map { it.toLong() }
                        .toList()
                    if (values.size == 3) {
                        val diff = values[0] - values[1]
                        mapList[mapIndex].add(
                            AlmanacEntry(
                                Pair(values[1], values[1] + values[2])
                            ) { l -> l + diff }
                        )

                    }
                }

            }
        }
        val threads = mutableListOf<Thread>()
        val results = mutableListOf<Pair<Long, Long>>()
        val indices = 0 until seeds.size step 2
        println(indices.toList())
        for (i in 0 until seeds.size step 2) {
            print(seeds[i])
            println(seeds[i + 1])
        }
        for (i in 0 until seeds.size step 2) {
            threads.add(
                thread(start = true) {
                    println("Thread started ${seeds[i]}")
                    var bestResult: Pair<Long, Long> = Pair(Long.MAX_VALUE, Long.MAX_VALUE)
                    val quarter = seeds[i + 1] / 4
                    for (l in seeds[i] until (seeds[i] + seeds[i + 1])) {
                        if (l % quarter == 0L) {
                            println("alive! ${seeds[i]}-$l")
                        }
                        val result = getResult(l)
                        if (result.second < bestResult.second) {
                            bestResult = result
                        }
                    }
                    results.add(bestResult)
                    println("Best result: ${bestResult}")
                }
            )
        }
        threads.forEach { it.join() }
        return results.minOf { it.second }
    }

    fun getResultInverse(startIndex: Long): Pair<Long, Long> {
        var currentIndex = startIndex
        mapList.asReversed().forEachIndexed { index, map ->
            currentIndex = getNextIndex(currentIndex, map)
        }
        return Pair(startIndex, currentIndex)
    }

    fun getResult(startIndex: Long): Pair<Long, Long> {
        var currentIndex = startIndex
        mapList.forEachIndexed { index, map ->
            currentIndex = getNextIndex(currentIndex, map)
        }
        return Pair(startIndex, currentIndex)
    }

    fun getNextIndex(sourceIndex: Long, map: MutableList<AlmanacEntry>): Long {
        val almanacEntry = map.find { it.rangePair.first <= sourceIndex && it.rangePair.second >= sourceIndex }
        return almanacEntry?.converter?.invoke(sourceIndex) ?: sourceIndex
    }
}

fun main() {
    println(Day5().doTheOtherThingINVERSE())
}