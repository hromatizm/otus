package command

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import motion.Angle
import motion.Vector
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.example.motion.velocity.ChangeVelocityAngleCmd
import org.example.motion.velocity.IObjWithChangeableVelocity
import kotlin.test.Test

class ChangeVelocityAngleCmdTest {

    @Test
    fun `when change velocity angle - set velocity with new angle`() {
        // Arrange
        val currentAngleMock = mockk<Angle>().also {
            every { it.getDegrees() } returns 10.0
        }
        val currentVelocityMock = mockk<Vector>().also {
            every { it.abs } returns 1
            every { it.angle } returns currentAngleMock
        }
        val newAngleMock = mockk<Angle>().also {
            every { it.getDegrees() } returns 20.0
        }
        val objMock = mockk<IObjWithChangeableVelocity>(relaxed = true).also {
            every { it.getVelocity() } returns currentVelocityMock
        }

        val testingCmd = ChangeVelocityAngleCmd(objMock, newAngleMock)

        // Act
        testingCmd.execute()

        // Assert
        verify { objMock.setVelocity(Vector(abs = 1, angle = Angle(degrees = 30.0))) }
    }

    @Test
    fun `exception after set velocity - also exception in the command`() {
        // Arrange
        val objMock = mockk<IObjWithChangeableVelocity>(relaxed = true).also {
            every { it.setVelocity(any()) } throws RuntimeException("Test exception")
        }
        val newAngleMock = mockk<Angle>(relaxed = true)
        val testingCmd = ChangeVelocityAngleCmd(objMock, newAngleMock)

        // Act
        val exception = catchThrowable {
            testingCmd.execute()
        }

        // Assert
        assertThat(exception)
            .isExactlyInstanceOf(RuntimeException::class.java)
            .hasMessage("Test exception")
    }
}
