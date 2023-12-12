package main.kotlin

class Day12 {

    val springRegex = Regex("#+")

    fun String.replaceUnknownWithTheKnown(known: String): String {
        var product = this
        known.forEach {
            product = product.replaceFirst('?', it)
        }
        return product
    }

    fun String.adheresToTheRules(groups: List<Long>): Boolean {
        val counts =
            springRegex.findAll(this).map { it.groupValues }.flatten().map { it.count { it == '#' }.toLong() }.toList()
        return groups == counts
    }

    fun String.findAllPossibleArrangements(): Long {
        val split = this.split(" ")
        val configuration = split[0]
        val groups = Util.numberRegex.findAll(split[1]).map { it.groupValues }.flatten().map { it.toLong() }.toList()
        val wildcards = configuration.count { it == '?' }.toLong()
        val necessarySprings = groups.sum() - configuration.count { it == '#' }
        val possibilities = getPossibleStrings(wildcards, necessarySprings)
        var result: Long = 0L
        possibilities.filter {
            configuration
                .replaceUnknownWithTheKnown(it)
                .adheresToTheRules(groups)
        }.forEach {
            result++
        }
        return result
    }

    fun getPossibleStrings(length: Long, necessarySprings: Long): List<String> {
        if (length == 0L) return listOf()
        var stringList = mutableListOf(".", "#")
        for (i in 1 until length) {
            stringList = stringList
                .map {
                    listOf("$it.", "$it#")
                }
                .flatten()
                .toMutableList()
        }
        return stringList.filter { it.count { it == '#' }.toLong() == necessarySprings }
    }

    fun doTheThing(): Long {
        val inputs = Util.getLinesFromFile("/day12/input.txt")
        return inputs.sumOf {
            it.findAllPossibleArrangements()
        }
    }

    fun String.expandTheSprings(): String {
        val split = this.split(" ")
        val yes = (0..4).map { split[1] }.toList()
        val no = (0..4).map { split[0] }.toList()
        val result = "${no.joinToString("?")} ${yes.joinToString(",")}"
        return result
    }

    fun doTheOtherThing(): Long {
        val inputs = Util.getLinesFromFile("/day12/test.txt")
        return inputs.sumOf {
            it.expandTheSprings().findAllPossibleArrangements()
        }
    }
}

fun main() {
    //println(Day12().doTheThing())
    println(Day12().doTheOtherThing())
}

