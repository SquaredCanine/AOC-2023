package main.kotlin

class Day9 {

    fun getDifferences(values: List<Long>): List<Long> {
        val result: MutableList<Long> = mutableListOf()
        for (i in 0 until (values.size - 1)) {
            result.add(
                values[i + 1] - values[i]
            )
        }
        return result.toList()
    }

    fun expandAndSum(numbers: List<List<Long>>, resolver: (List<Long>, List<List<Long>>) -> Long): Long {
        return numbers.map {
            var latest = it
            val expanded = mutableListOf<List<Long>>()
            while (!latest.all { it == 0L }) {
                latest = getDifferences(latest)
                if (latest.isEmpty()) {
                    latest = listOf(0)
                }
                expanded.add(latest)
            }
            resolver.invoke(it, expanded.reversed())
        }.sum()
    }

    fun doTheThing(): Long {
        val inputs = Util.getLinesFromFile("/day9/input.txt")
        val numbers =
            inputs.map { Util.numberRegex.findAll(it).map { it.groupValues }.flatten().map { it.toLong() }.toList() }
        return expandAndSum(numbers) { originalList, expanded ->
            val increment = expanded
                .map {
                    it.last()
                }
                .fold(0L) { acc, l -> acc + l }
            originalList.last() + increment
        }
    }

    fun doTheOtherThing(): Long {
        val inputs = Util.getLinesFromFile("/day9/input.txt")
        val numbers =
            inputs.map { Util.numberRegex.findAll(it).map { it.groupValues }.flatten().map { it.toLong() }.toList() }
        return expandAndSum(numbers) { originalList, expanded ->
            val increment = expanded
                .map {
                    it.first()
                }
                .fold(0L) { acc, l -> l - acc }
            originalList.first() - increment
        }
    }
}

fun main() {
    println(Day9().doTheThing())
    println(Day9().doTheOtherThing())
}