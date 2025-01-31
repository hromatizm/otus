package solver

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.example.solve.equalsTo
import org.example.solve.Solver
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SolveTest {

    val solver = Solver()

    @Test
    fun `discriminant less than 0 - should return empty array`() {
        // Arrange (x^2+1)
        val a = 1.0
        val b = 0.0
        val c = 1.0

        // Act
        val result = solver.solve(a, b, c)

        // Assert
        assertThat(result).isEmpty()
    }

    @Test
    fun `discriminant greater than 0 - should return 2 roots`() {
        // Arrange (x^2-1)
        val a = 1.0
        val b = 0.0
        val c = -1.0

        // Act
        val result = solver.solve(a, b, c)

        // Assert
        assertThat(result[0].equalsTo(1.0)).isTrue
        assertThat(result[1].equalsTo(-1.0)).isTrue
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "1.0, 2.0, 1.0",                //(x^2+2x+1)
            "2.0, 4.0, 1.999_999_999_99",   // меньше, чем эпсилон
        ]
    )
    fun `discriminant is 0 - should return 1 root`(a: Double, b: Double, c: Double) {
        // Act
        val result = solver.solve(a, b, c)

        // Assert
        assertThat(result[0].equalsTo(-1.0)).isTrue
        assertThat(result[1].equalsTo(-1.0)).isTrue
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.0, 0.000_000_000_1, -0.000_000_000_1])
    fun `a is 0 - should throw IllegalArgumentException`(a: Double) {
        // Arrange
        val b = 1.0
        val c = 1.0

        // Act
        val exception = catchThrowable {
            solver.solve(a, b, c)
        }

        // Assert
        assertThat(exception)
            .isExactlyInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("first argument cannot be zero")
    }

    @ParameterizedTest
    @MethodSource("argumentIsNotRelevant_args")
    fun `argument is not relevant - should throw IllegalArgumentException`(
        a: Double,
        b: Double,
        c: Double,
        expectedMessage: String
    ) {
        // Act
        val exception = catchThrowable {
            solver.solve(a, b, c)
        }

        // Assert
        assertThat(exception)
            .isExactlyInstanceOf(IllegalArgumentException::class.java)
            .hasMessage(expectedMessage)
    }

    private fun argumentIsNotRelevant_args() = listOf(
        Arguments.of(Double.NaN, 1.0, 1.0, "Argument cannot be NaN"),
        Arguments.of(1.0, Double.NaN, 1.0, "Argument cannot be NaN"),
        Arguments.of(1.0, 1.0, Double.NaN, "Argument cannot be NaN"),

        Arguments.of(Double.POSITIVE_INFINITY, 1.0, 1.0, "Argument cannot be positive infinite"),
        Arguments.of(1.0, Double.POSITIVE_INFINITY, 1.0, "Argument cannot be positive infinite"),
        Arguments.of(1.0, 1.0, Double.POSITIVE_INFINITY, "Argument cannot be positive infinite"),

        Arguments.of(Double.NEGATIVE_INFINITY, 1.0, 1.0, "Argument cannot be negative infinite"),
        Arguments.of(1.0, Double.NEGATIVE_INFINITY, 1.0, "Argument cannot be negative infinite"),
        Arguments.of(1.0, 1.0, Double.NEGATIVE_INFINITY, "Argument cannot be negative infinite"),
    )
}
