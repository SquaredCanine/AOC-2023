package main.kotlin

class Util {

    data class Coordinate(val x: Int, val y: Int)

    companion object {
        val numberRegex = Regex("-*\\d+")

        fun getLinesFromFile(file: String): List<String> {
            return Util::class.java.getResourceAsStream(file).bufferedReader().readLines()
        }

        fun List<String>.transpose(): List<String> {
            val cols = this[0].length
            val rows = this.size
            return List(cols) { j ->
                List(rows) { i ->
                    this[i][j]
                }
            }.map {
                it.joinToString("")
            }
        }

        fun <T> Coordinate.isValid(layout: List<List<T>>): Boolean {
            return this.x in layout[0].indices &&
                    this.y in layout.indices
        }
    }
}