package command

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import exception.CommandException
import motion.fuel.CheckFuelCmd
import motion.fuel.IFuelConsumer
import kotlin.test.Test

class CheckFuelCmdTest {

    @Test
    fun `check fuel - enough`() {
        // Arrange
        val objMock = mockk<IFuelConsumer>().also {
            every { it.getFuel() } returns 2.0
        }
        val testingCmd = CheckFuelCmd(obj = objMock, minValue = 1.5)

        // Act
        val exception = catchThrowable {
            testingCmd.execute()
        }

        // Assert
        assertThat(exception).isNull()
    }

    @Test
    fun `check fuel - not enough`() {
        // Arrange
        val objMock = mockk<IFuelConsumer>().also {
            every { it.getFuel() } returns 1.0
        }
        val testingCmd = CheckFuelCmd(obj = objMock, minValue = 1.5)

        // Act
        val exception = catchThrowable {
            testingCmd.execute()
        }

        // Assert
        assertThat(exception)
            .isExactlyInstanceOf(CommandException::class.java)
            .hasMessage("Not enough fuel")
    }

    @Test
    fun `when exception in the object - also exception in the command`() {
        // Arrange
        val objMock = mockk<IFuelConsumer>().also {
            every { it.getFuel() } throws RuntimeException("Test exception")
        }
        val testingCmd = CheckFuelCmd(obj = objMock, minValue = 1.5)

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