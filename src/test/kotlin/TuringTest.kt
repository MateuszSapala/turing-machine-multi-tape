import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import pl.lodz.uni.project1.turing.Turing
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class TuringTest {
    companion object {
        val ACCEPTED = listOf(1, 4, 9, 16, 25, 36, 49, 64, 81, 100)
    }

    @ParameterizedTest
    @MethodSource("testCheckIfNumberIsPowerOfNumberData")
    fun testCheckIfNumberIsPowerOfNumber(number: Int) {
        assertDoesNotThrow(Exec(number))
    }

    private fun testCheckIfNumberIsPowerOfNumberData(): Stream<Arguments> = ACCEPTED
        .map { Arguments.of(it) }
        .stream()

    @ParameterizedTest
    @MethodSource("testCheckIfNumberIsPowerOfNumberThrowData")
    fun testCheckIfNumberIsPowerOfNumberThrow(number: Int) {
        assertThrows(IllegalArgumentException().javaClass, Exec(number))
    }

    private fun testCheckIfNumberIsPowerOfNumberThrowData(): Stream<Arguments> = IntArray(100) { it }
        .toList()
        .filter { !ACCEPTED.contains(it) }
        .map { Arguments.of(it) }
        .stream()

    private class Exec(val number: Int) : Executable {
        override fun execute() {
            Turing(number).checkIfNumberIsPowerOfNumber()
        }
    }
}