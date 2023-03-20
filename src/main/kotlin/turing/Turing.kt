package pl.lodz.uni.project1.turing

import pl.lodz.uni.project1.turing.Move.*
import pl.lodz.uni.project1.turing.State.*
import pl.lodz.uni.project1.turing.Value.A
import pl.lodz.uni.project1.turing.Value.EMPTY

class Turing private constructor(
    private val tapes: Array<Array<Value>>,
    private var indexes: IntArray,
    private var state: State,
    private var iteration: Int = 1
) {
    companion object {
        operator fun invoke(number: Int): Turing {
            val array = Array(4) { Array(number * 3 + 2) { EMPTY } }
            for (i in number until number * 2) {
                array[0][i] = A
            }
            return Turing(array, intArrayOf(number, number, number, number), S0);
        }
    }

    private var currentValues: TapeValues
        get() {
            val values = TapeValues()
            for (i in 0..3) {
                values.array[i] = tapes[i][indexes[i]]
            }
            return values
        }
        set(values) {
            for (i in 0..3) {
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
                        TapeValues(A, EMPTY, EMPTY, EMPTY) -> next(S1, TapeValues(A, A, A, EMPTY), arrayOf(S, S, S, S))
                        else -> throwBadArgument()
                    }
                }

                S1 -> {
                    when (currentValues) {
                        TapeValues(A, A, A, EMPTY) -> next(S2, TapeValues(A, A, A, A), arrayOf(S, S, R, R))
                        else -> throwBadArgument()
                    }
                }

                S2 -> {
                    when (currentValues) {
                        TapeValues(A, A, A, EMPTY) -> next(S2, TapeValues(A, A, A, A), arrayOf(S, S, R, R))
                        TapeValues(A, A, EMPTY, EMPTY) -> next(S3, TapeValues(A, A, EMPTY, EMPTY), arrayOf(S, R, L, S))
                        TapeValues(A, EMPTY, A, EMPTY) -> next(S4, TapeValues(A, EMPTY, A, EMPTY), arrayOf(S, L, S, L))
                        else -> throwBadArgument()
                    }
                }

                S3 -> {
                    when (currentValues) {
                        TapeValues(A, A, A, EMPTY) -> next(S3, TapeValues(A, A, A, A), arrayOf(S, S, L, R))
                        TapeValues(A, A, EMPTY, EMPTY) -> next(S2, TapeValues(A, A, EMPTY, EMPTY), arrayOf(S, R, R, S))
                        TapeValues(A, EMPTY, A, EMPTY) -> next(S4, TapeValues(A, EMPTY, A, EMPTY), arrayOf(S, L, S, L))
                        else -> throwBadArgument()
                    }
                }

                S4 -> {
                    when (currentValues) {
                        TapeValues(A, A, A, A) -> next(S4, TapeValues(A, A, A, EMPTY), arrayOf(R, L, L, L))
                        TapeValues(A, A, EMPTY, A) -> next(S4, TapeValues(A, A, EMPTY, EMPTY), arrayOf(R, L, S, L))
                        TapeValues(A, EMPTY, A, A) -> next(S4, TapeValues(A, EMPTY, A, EMPTY), arrayOf(R, S, L, L))
                        TapeValues(A, EMPTY, EMPTY, A) -> next(
                            S4,
                            TapeValues(A, EMPTY, EMPTY, EMPTY),
                            arrayOf(R, S, S, L)
                        )

                        TapeValues(A, EMPTY, EMPTY, EMPTY) -> next(S5, TapeValues(A, A, A, EMPTY), arrayOf(L, S, S, S))
                        TapeValues(EMPTY, EMPTY, EMPTY, EMPTY) -> next(
                            Sk,
                            TapeValues(EMPTY, EMPTY, EMPTY, EMPTY),
                            arrayOf(S, S, S, S)
                        )

                        else -> throwBadArgument()
                    }
                }

                S5 -> {
                    when (currentValues) {
                        TapeValues(A, A, A, EMPTY) -> next(S6, TapeValues(A, A, A, EMPTY), arrayOf(L, S, S, S))
                        else -> throwBadArgument()
                    }
                }

                S6 -> {
                    when (currentValues) {
                        TapeValues(A, A, A, EMPTY) -> next(S6, TapeValues(A, A, A, EMPTY), arrayOf(L, S, S, S))
                        TapeValues(EMPTY, A, A, EMPTY) -> next(S2, TapeValues(EMPTY, A, A, EMPTY), arrayOf(R, S, S, S))
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
            "$iteration:\t(${state.value},${c.array[0].value},${c.array[1].value},${c.array[2].value},${c.array[3].value})->" +
                    "(${newState.value},${newValues.array[0].value},${newValues.array[1].value},${newValues.array[2].value},${newValues.array[3].value}," +
                    "${moves[0].string},${moves[1].string},${moves[2].string},${moves[3].string})"
        )
        println("$this")
        iteration++
        state = newState
        currentValues = newValues
        for (i in 0..3) {
            indexes[i] = indexes[i] + moves[i].value
        }
    }

    override fun toString(): String {
        var s = ""
        for (i in 0..3) {
            for (j in tapes[i].indices) {
                if (j == indexes[i]) {
                    s += "($state)"
                }
                s += tapes[i][j].value
            }
            if (i != 3) s += "\n"
        }
        return s
    }
}