package motion

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import motion.move.IMoveable
import motion.move.MoveCmd
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MoveTest {

    @Test
    fun `move correctly with vector (-7, 3)`() {
        // Arrange
        val angleMock = mockk<Angle>().also {
            every { it.getDegrees() } returns 156.8
        }
        val velocityMock = mockk<Vector>().also { //  vector (-7, 3)
            every { it.angle } returns angleMock
            every { it.abs } returns 8
        }
        val initialLocationMock = mockk<Point>().also {
            every { it.x } returns 12
            every { it.y } returns 5
        }
        val movingObjMock = mockk<IMoveable>(relaxed = true).also {
            every { it.getVelocity() } returns velocityMock
            every { it.getLocation() } returns initialLocationMock
        }
        val testingMoveCmd = MoveCmd(movingObjMock)

        // Act
        testingMoveCmd.execute()

        // Assert
        verify { movingObjMock.getVelocity() }
        verify { movingObjMock.getLocation() }
        verify {
            movingObjMock.setLocation(
                newLocation = withArg {
                    assertThat(it).isEqualTo(Point(x = 5, y = 8))
                }
            )
        }
        confirmVerified(movingObjMock)
    }

    @Test
    fun `impossible to get location - should throw Exception`() {
        // Arrange
        val movingObjMock = mockk<IMoveable>(relaxed = true).also {
            every { it.getLocation() } throws RuntimeException("Unable to get location")
        }
        val testingMoveCmd = MoveCmd(movingObjMock)

        // Act
        val exception = catchThrowable {
            testingMoveCmd.execute()
        }

        // Assert
        assertThat(exception)
            .isExactlyInstanceOf(RuntimeException::class.java)
            .hasMessage("Unable to get location")
    }

    @Test
    fun `impossible to get velocity - should throw Exception`() {
        // Arrange
        val movingObjMock = mockk<IMoveable>(relaxed = true).also {
            every { it.getVelocity() } throws RuntimeException("Unable to get velocity")
        }
        val testingMoveCmd = MoveCmd(movingObjMock)

        // Act
        val exception = catchThrowable {
            testingMoveCmd.execute()
        }

        // Assert
        assertThat(exception)
            .isExactlyInstanceOf(RuntimeException::class.java)
            .hasMessage("Unable to get velocity")
    }

    @Test
    fun `impossible to set location - should throw Exception`() {
        // Arrange
        val movingObjMock = mockk<IMoveable>(relaxed = true).also {
            every { it.setLocation(any()) } throws RuntimeException("Unable to set location")
        }
        val testingMoveCmd = MoveCmd(movingObjMock)

        // Act
        val exception = catchThrowable {
            testingMoveCmd.execute()
        }

        // Assert
        assertThat(exception)
            .isExactlyInstanceOf(RuntimeException::class.java)
            .hasMessage("Unable to set location")
    }
}
