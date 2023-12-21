package main.kotlin

import java.util.*

enum class Signal { HIGH, LOW }

abstract class Module(val label: String, var parents: List<String>, val children: List<String>) {

    open fun addParent(parent: String) {
        parents += parent
    }

    abstract fun parsePulse(signal: Signal, source: String): Signal?
}


class FlipFlop(label: String, parents: List<String>, children: List<String>) : Module(label, parents, children) {

    var isLow: Boolean = true
    override fun parsePulse(signal: Signal, source: String): Signal? {
        return when (signal) {
            Signal.LOW -> {
                isLow = !isLow
                if (isLow) {
                    Signal.LOW
                } else {
                    Signal.HIGH
                }
            }

            else -> {
                null
            }
        }
    }

}

class Conjunction(label: String, parents: List<String>, children: List<String>) : Module(label, parents, children) {

    var inputs: MutableMap<String, Signal> = super.parents.associate {
        it to Signal.LOW
    }.toMutableMap()

    override fun parsePulse(signal: Signal, source: String): Signal {
        inputs[source] = signal
        return if (inputs.all { it.value == Signal.HIGH }) {
            Signal.LOW
        } else {
            Signal.HIGH
        }
    }

    override fun addParent(parent: String) {
        parents += parent
        inputs = parents.associate {
            it to Signal.LOW
        }.toMutableMap()
    }
}

class Broadcast(label: String, parents: List<String>, children: List<String>) : Module(label, parents, children) {
    override fun parsePulse(signal: Signal, source: String): Signal {
        return signal
    }
}

class Day20 {

    fun getInput(): Map<String, Module> {
        val input = Util.getLinesFromFile("/day20/input.txt")
        val map = mutableMapOf<String, Module>()
        val parentToChild = mutableListOf<Pair<String, List<String>>>()
        input.forEach {
            val split = it.split(" -> ")
            val children = split[1].split(", ")
            var label = split[0]
            when (split[0].first()) {
                'b' -> {
                    map[label] = Broadcast(
                        label, listOf(), children
                    )
                }

                '%' -> {
                    label = split[0].substring(1)
                    map[label] = FlipFlop(
                        label, listOf(), children
                    )
                }

                '&' -> {
                    label = split[0].substring(1)
                    map[label] = Conjunction(
                        label, listOf(), children
                    )
                }
            }
            parentToChild.add(Pair(label, children))
        }
        parentToChild.forEach { (parent, children) ->
            children.forEach { child ->
                map[child]?.addParent(parent)
            }
        }
        return map
    }

    data class Pulse(val origin: String, val signal: Signal, val destinations: List<String>)

    fun part1(): Long {
        val map = getInput()
        println(map)
        var high = 0L
        var low = 0L

        val queueueueueue: Queue<Pulse> = LinkedList()

        for (i in 0..999) {
            queueueueueue.add(
                Pulse(
                    "button", Signal.LOW, listOf("broadcaster")
                )
            )
            while (queueueueueue.peek() != null) {
                val pulse = queueueueueue.poll()

                if (pulse.signal == Signal.LOW) {
                    low += pulse.destinations.count()
                } else {
                    high += pulse.destinations.count()
                }

                pulse.destinations.forEach {
                    val receiver = map[it]
                    val result = receiver?.parsePulse(pulse.signal, pulse.origin)
                    if (result != null) {
                        queueueueueue.add(
                            Pulse(
                                receiver.label,
                                result,
                                receiver.children
                            )
                        )
                    }
                }
            }
        }
        return low * high
    }

    fun MutableMap<String, Module>.toKey(): String {
        var key = ""
        this.values.sortedBy { it.label }.forEach {
            key += "["
            key += "${it.label},"
            when(it) {
                is Broadcast -> {
                    key += "Broadcast,"
                }
                is Conjunction -> {
                    key += "Conjunction,"
                    key += "("
                    it.inputs.toSortedMap().forEach { parent, value ->
                        key += "$parent:$value,"
                    }
                    key += ")]"
                }
                is FlipFlop -> {
                    key += "FlipFlop,"
                    key += "${it.isLow}]"
                }
            }
        }
        return key
    }

    fun part2(): Long {
        val subCycleMap = mutableMapOf<String, Int>()
        for (i in 0..3) {
            val map = getInput().toMutableMap()
            var repetitionFound = false

            val childName = map["broadcaster"]!!.children[i]

            map["broadcaster"] = Broadcast(
                map["broadcaster"]!!.label,
                listOf("button"),
                listOf(childName)
            )
            val states = mutableMapOf<String, Int>()
            val queueueueueue: Queue<Pulse> = LinkedList()
            var buttonpresses = 0

            while (!repetitionFound) {
                if (states[map.toKey()] != null) {
                    println("repetition: $buttonpresses")
                    subCycleMap[childName] = buttonpresses - 1
                    repetitionFound = true
                } else {
                    states[map.toKey()] = 0
                }
                queueueueueue.add(
                    Pulse(
                        "button", Signal.LOW, listOf("broadcaster")
                    )
                )
                buttonpresses++
                while (queueueueueue.peek() != null) {
                    val pulse = queueueueueue.poll()

                    pulse.destinations.forEach {
                        val receiver = map[it]
                        val result = receiver?.parsePulse(pulse.signal, pulse.origin)
                        if (result != null) {
                            queueueueueue.add(
                                Pulse(
                                    receiver.label,
                                    result,
                                    receiver.children
                                )
                            )
                        }
                    }
                }
            }
        }
        return subCycleMap.values.fold(1) { acc, iter -> acc * iter }
    }
}

fun main() {
    println(Day20().part1())
    println(Day20().part2())
}