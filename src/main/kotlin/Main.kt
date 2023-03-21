package pl.lodz.uni.project1

import pl.lodz.uni.project1.turing.Turing

fun main() {
    println("Enter the number you want to check if it is a power of 2:")
    val number = readln().toInt()
    Turing(number).checkIfNumberIsPowerOfNumber()
    println("Your number power of 2")
}