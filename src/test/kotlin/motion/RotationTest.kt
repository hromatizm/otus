package motion

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import motion.rotate.IRotatable
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.example.motion.rotate.Rotation
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RotationTest {

    @ParameterizedTest
    @CsvSource(
        value = [
            "0.0, 100.0",
            "360.0, 460.0",
            "-360.0, -260.0",
            "20.5, 120.5",
            "-20.5, 79.5",
            "400.0, 500.0",
            "-400.0, -300.0",
        ]
    )
    fun `rotate correctly from 100 degrees`(delta: Double, expectedResult: Double) {
        // Arrange
        val angleMock = mockk<Angle>().also {
            every { it.getDegrees() } returns 100.0
        }
        val rotatingObjMock = mockk<IRotatable>(relaxed = true).also {
            every { it.getAngle() } returns angleMock
        }
        val testingRotation = Rotation()

        // Act
        testingRotation.execute(rotatingObjMock, delta)

        // Assert
        verify { rotatingObjMock.getAngle() }
        verify {
            rotatingObjMock.setAngle(
                newAngle = withArg {
                    assertThat(it).isEqualTo(Angle(degrees = expectedResult))
                }
            )
        }
        confirmVerified(rotatingObjMock)
    }

    @Test
    fun `impossible to get angle - should throw Exception`() {
        // Arrange
        val rotatingObjMock = mockk<IRotatable>(relaxed = true).also {
            every { it.getAngle() } throws RuntimeException("Unable to get angle")
        }
        val degreesDelta = 20.0
        val testingRotation = Rotation()

        // Act
        val exception = catchThrowable {
            testingRotation.execute(rotatingObjMock, degreesDelta)
        }

        // Assert
        assertThat(exception)
            .isExactlyInstanceOf(RuntimeException::class.java)
            .hasMessage("Unable to get angle")
    }

    @Test
    fun `impossible to set angle - should throw Exception`() {
        // Arrange
        val rotatingObjMock = mockk<IRotatable>(relaxed = true).also {
            every { it.setAngle(any()) } throws RuntimeException("Unable to set angle")
        }
        val degreesDelta = 20.0
        val testingRotation = Rotation()

        // Act
        val exception = catchThrowable {
            testingRotation.execute(rotatingObjMock, degreesDelta)
        }

        // Assert
        assertThat(exception)
            .isExactlyInstanceOf(RuntimeException::class.java)
            .hasMessage("Unable to set angle")
    }
}
