package pl.lodz.uni.project1.turing

enum class Move(val value: Int, val string: String) {
    R(1, "R"),
    L(-1, "L"),
    S(0, "S");
}