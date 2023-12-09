package main.kotlin

class Util {

    companion object {
        val numberRegex = Regex("-*\\d+")

        fun getLinesFromFile(file: String): List<String> {
            return Util::class.java.getResourceAsStream(file).bufferedReader().readLines()
        }
    }
}