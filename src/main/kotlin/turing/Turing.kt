package pl.lodz.uni.project1.turing

import pl.lodz.uni.project1.turing.Move.*
import pl.lodz.uni.project1.turing.State.*
import pl.lodz.uni.project1.turing.Value.*
import kotlin.math.sqrt

class Turing private constructor(
    private val tapes: Array<Array<Value>>,
    private var indexes: IntArray,
    private var state: State,
    private var iteration: Int = 1
) {
    companion object {
        operator fun invoke(number: Int): Turing {
            val a1 = Array(number + 2) { A }
            a1[0] = EMPTY
            a1[a1.lastIndex] = EMPTY
            val tapesSize = sqrt(number.toDouble()).toInt() + 3
            val a2 = Array(tapesSize) { EMPTY }
            val a3 = a2.copyOf()
            return Turing(arrayOf(a1, a2, a3), intArrayOf(1, tapesSize - 2, tapesSize - 2), S0);
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
                        TapeValues(A, EMPTY, EMPTY) -> next(S1, TapeValues(A, A, A), arrayOf(S, S, S))
                        else -> throwBadArgument()
                    }
                }

                S1 -> {
                    when (currentValues) {
                        TapeValues(A, A, A) -> next(S1, TapeValues(B, A, A), arrayOf(R, S, R))
                        TapeValues(A, A, EMPTY) -> next(S2, TapeValues(A, A, EMPTY), arrayOf(S, R, L))
                        TapeValues(EMPTY, A, EMPTY) -> next(S2, TapeValues(EMPTY, A, EMPTY), arrayOf(S, R, L))
                        TapeValues(A, EMPTY, A) -> next(S3, TapeValues(A, EMPTY, A), arrayOf(L, L, L))
                        TapeValues(EMPTY, EMPTY, A) -> next(Sk, TapeValues(EMPTY, EMPTY, A), arrayOf(S, S, S))
                        else -> throwBadArgument()
                    }
                }

                S2 -> {
                    when (currentValues) {
                        TapeValues(A, A, A) -> next(S2, TapeValues(B, A, A), arrayOf(R, S, L))
                        TapeValues(A, A, EMPTY) -> next(S1, TapeValues(A, A, EMPTY), arrayOf(S, R, R))
                        TapeValues(EMPTY, A, EMPTY) -> next(S1, TapeValues(EMPTY, A, EMPTY), arrayOf(S, R, R))
                        TapeValues(A, EMPTY, A) -> next(S3, TapeValues(A, EMPTY, A), arrayOf(L, L, L))
                        TapeValues(EMPTY, EMPTY, A) -> next(Sk, TapeValues(EMPTY, EMPTY, A), arrayOf(S, S, S))
                        else -> throwBadArgument()
                    }
                }

                S3 -> {
                    when (currentValues) {
                        TapeValues(B, A, A) -> next(S3, TapeValues(A, A, A), arrayOf(L, L, L))
                        TapeValues(B, A, EMPTY) -> next(S3, TapeValues(A, A, EMPTY), arrayOf(L, L, S))
                        TapeValues(B, EMPTY, EMPTY) -> next(S3, TapeValues(A, EMPTY, EMPTY), arrayOf(L, S, S))
                        TapeValues(EMPTY, EMPTY, EMPTY) -> next(S1, TapeValues(EMPTY, A, A), arrayOf(R, S, S))
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
            "$iteration:\t(${state.value},${c.array[0].value},${c.array[1].value},${c.array[2].value})->" +
                    "(${newState.value},${newValues.array[0].value},${newValues.array[1].value},${newValues.array[2].value}," +
                    "${moves[0].string},${moves[1].string},${moves[2].string})"
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