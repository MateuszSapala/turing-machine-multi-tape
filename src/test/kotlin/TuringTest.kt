import org.junit.Ignore
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
    @ParameterizedTest
    @MethodSource("testCheckIfNumberIsPowerOfNumberData")
    fun testCheckIfNumberIsPowerOfNumber(number: Int) {
        assertDoesNotThrow(Exec(number))
    }

    private fun testCheckIfNumberIsPowerOfNumberData(): Stream<Arguments?>? {
        return Stream.of(
            Arguments.of(1),
            Arguments.of(4),
            Arguments.of(9),
            Arguments.of(16),
            Arguments.of(25),
            Arguments.of(36),
            Arguments.of(49),
            Arguments.of(64),
            Arguments.of(81),
            Arguments.of(100),
        )
    }

    @ParameterizedTest
    @MethodSource("testCheckIfNumberIsPowerOfNumberThrowData")
    fun testCheckIfNumberIsPowerOfNumberThrow(number: Int) {
        assertThrows(IllegalArgumentException().javaClass, Exec(number))
    }

    private fun testCheckIfNumberIsPowerOfNumberThrowData(): Stream<Arguments?>? {
        return Stream.of(
            Arguments.of(0),
            Arguments.of(2),
            Arguments.of(3),
            Arguments.of(5),
            Arguments.of(6),
            Arguments.of(7),
            Arguments.of(8),
            Arguments.of(10),
            Arguments.of(44),
            Arguments.of(99),
        )
    }

    private class Exec(val number: Int) : Executable {
        override fun execute() {
            Turing(number).checkIfNumberIsPowerOfNumber()
        }
    }
}