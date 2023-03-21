package pl.lodz.uni.project1.turing

import pl.lodz.uni.project1.turing.Move.*
import pl.lodz.uni.project1.turing.State.*
import pl.lodz.uni.project1.turing.Value.*

class Turing private constructor(
    private val tapes: Array<Array<Value>>,
    private var indexes: IntArray,
    private var state: State,
    private var iteration: Int = 1
) {
    companion object {
        operator fun invoke(number: Int): Turing {
            val a1 = Array(number * 2 + 2) { EMPTY }
            for (i in 1..number) {
                a1[i] = A
            }
            val a2 = Array(number * 2 + 2) { EMPTY }
            return Turing(arrayOf(a1, a2), intArrayOf(1, 1), S0);
        }
    }

    private var currentValues: TapeValues
        get() {
            val values = TapeValues()
            for (i in tapes.indices) {
                values.array[i] = tapes[i][indexes[i]]
            }
            return values
        }
        set(values) {
            for (i in tapes.indices) {
                tapes[i][indexes[i]] = values.array[i]
            }
        }

    private fun throwBadArgument() {
        println("Latest tape state: \n$this")
        throw IllegalArgumentException("(${state.value},${currentValues})")
    }

    fun checkIfNumberIsPowerOfNumber() {
        while (true) {
            when (state) {
                S0 -> {
                    when (currentValues) {
                        TapeValues(A, EMPTY) -> next(S1, TapeValues(A, A), arrayOf(S, R))
                        else -> throwBadArgument()
                    }
                }

                S1 -> {
                    when (currentValues) {
                        TapeValues(A, EMPTY) -> next(S2, TapeValues(A, A), arrayOf(S, L))
                        else -> throwBadArgument()
                    }
                }

                S2 -> {
                    when (currentValues) {
                        TapeValues(A, A) -> next(S2, TapeValues(B, B), arrayOf(R, R))
                        TapeValues(A, EMPTY) -> next(S3, TapeValues(A, EMPTY), arrayOf(L, S))
                        TapeValues(EMPTY, EMPTY) -> next(Sk, TapeValues(EMPTY, EMPTY), arrayOf(S, S))
                        else -> throwBadArgument()
                    }
                }

                S3 -> {
                    when (currentValues) {
                        TapeValues(B, EMPTY) -> next(S3, TapeValues(A, A), arrayOf(L, R))
                        TapeValues(EMPTY, EMPTY) -> next(S4, TapeValues(EMPTY, EMPTY), arrayOf(R, L))
                        else -> throwBadArgument()
                    }
                }

                S4 -> {
                    when (currentValues) {
                        TapeValues(A, A), TapeValues(A, B) -> next(S4, TapeValues(A, A), arrayOf(S, L))
                        TapeValues(A, EMPTY) -> next(S2, TapeValues(A, EMPTY), arrayOf(S, R))
                        else -> throwBadArgument()
                    }
                }

                Sk -> {
                    println("Ends with: \n$this")
                    return
                }
            }
        }
    }

    private fun next(
        newState: State, newValues: TapeValues, moves: Array<Move>,
    ) {
        val c = currentValues
        println(
            "$iteration:\t(${state.value},${c.array[0].value},${c.array[1].value})->" +
                    "(${newState.value},${newValues.array[0].value},${newValues.array[1].value}," +
                    "${moves[0].string},${moves[1].string})"
        )
        println("$this")
        iteration++
        state = newState
        currentValues = newValues
        for (i in tapes.indices) {
            indexes[i] = indexes[i] + moves[i].value
        }
    }

    override fun toString(): String {
        var s = ""
        for (i in tapes.indices) {
            for (j in tapes[i].indices) {
                if (j == indexes[i]) {
                    s += "($state)"
                }
                s += tapes[i][j].value
            }
            if (i != tapes.lastIndex) s += "\n"
        }
        return s
    }
}