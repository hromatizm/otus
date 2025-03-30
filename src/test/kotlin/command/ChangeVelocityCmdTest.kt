package command

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import motion.Vector
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import motion.velocity.ChangeVelocityCmd
import motion.velocity.IObjWithChangeableVelocity
import kotlin.test.Test

class ChangeVelocityCmdTest {

    @Test
    fun `when change velocity - set new velocity to object`() {
        // Arrange
        val objMock = mockk<IObjWithChangeableVelocity>(relaxed = true)
        val newVelocityMock = mockk<Vector>()
        val testingCmd = ChangeVelocityCmd(objMock, newVelocityMock)

        // Act
        testingCmd.execute()

        // Assert
        verify { objMock.setVelocity(newVelocityMock) }
    }

    @Test
    fun `exception after set velocity - also exception in the command`() {
        // Arrange
        val objMock = mockk<IObjWithChangeableVelocity>(relaxed = true).also {
            every { it.setVelocity(any()) } throws RuntimeException("Test exception")
        }
        val newVelocityMock = mockk<Vector>()
        val testingCmd = ChangeVelocityCmd(objMock, newVelocityMock)

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
