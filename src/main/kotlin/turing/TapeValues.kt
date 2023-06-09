package pl.lodz.uni.project1.turing

import pl.lodz.uni.project1.turing.Value.EMPTY

data class TapeValues(val array: Array<Value>) {
    constructor() : this(Array(2) { EMPTY })
    constructor(v1: Value, v2: Value) : this(arrayOf(v1, v2))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TapeValues

        if (!array.contentEquals(other.array)) return false

        return true
    }

    override fun hashCode(): Int {
        return array.contentHashCode()
    }
}
