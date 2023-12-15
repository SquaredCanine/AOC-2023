package main.kotlin

class Day15 {

    data class LensEntry(val label: String, var strength: Int)
    val lensMap = mutableMapOf<Int, List<LensEntry>>()

    fun String.getHash(): Int {
        return this.fold(0) { acc, char ->
            var result = acc + char.code
            result *= 17
            result %= 256
            result
        }
    }

    fun getInput(): String {
        return Util.getLinesFromFile("/day15/input.txt")[0]
    }

    fun doTheThing(): Long {
        val input = getInput()
        return input.split(",")
            .sumOf {
                it.getHash().toLong()
            }
    }

    fun doTheOtherThing(): Long {
        val input = getInput()
        input.split(",").forEach {
            if (it.contains("-")) {
                val label = it.replace("-", "")
                val boxNumber = label.getHash()
                val box = lensMap[boxNumber]
                if (box != null) {
                    lensMap[boxNumber] = box.filter { (_label, _) -> _label != label }
                }
            } else {
                val labelAndStrength = it.split("=")
                val label = labelAndStrength[0]
                val strength = labelAndStrength[1].toInt()
                val boxNumber = label.getHash()
                val box = lensMap[boxNumber]
                if (box != null) {
                    if (box.none { (_label, _) -> _label == label }) {
                        lensMap[boxNumber] = box + LensEntry(label, strength)
                    } else {
                        val newList = box.toMutableList()
                        newList.find { it.label == label }?.strength = strength
                        lensMap[boxNumber] = newList

                    }
                } else {
                    lensMap[boxNumber] = listOf(LensEntry(label, strength))
                }
            }
        }
        val result = lensMap.map {
            val multiplicationFactor = it.key + 1
            it.value.mapIndexed { index, lens ->
                (index + 1) * lens.strength * multiplicationFactor
            }.sum().toLong()
        }.sum()
        return result
    }
}

fun main() {
    println(Day15().doTheThing())
    println(Day15().doTheOtherThing())
}