package command

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import exception.CommandException
import motion.fuel.CheckFuelCmd
import motion.fuel.IFuelConsumer
import motion.fuel.BurnFuelCmd
import kotlin.test.Test

class BurnFuelCmdTest {

    @Test
    fun `when burn fuel it become less`() {
        // Arrange
        val objMock = mockk<IFuelConsumer>(relaxed = true).also {
            every { it.getFuel() } returns 10.0
        }
        val testingCmd = BurnFuelCmd(obj = objMock, value = 1.5)

        // Act
        testingCmd.execute()

        // Assert
        verify { objMock.setFuel(8.5) }
    }

    @Test
    fun `exception after get fuel - also exception in the command`() {
        // Arrange
        val objMock = mockk<IFuelConsumer>().also {
            every { it.getFuel() } throws RuntimeException("Test exception")
        }
        val testingCmd = BurnFuelCmd(obj = objMock, value = 1.5)

        // Act
        val exception = catchThrowable {
            testingCmd.execute()
        }

        // Assert
        assertThat(exception)
            .isExactlyInstanceOf(RuntimeException::class.java)
            .hasMessage("Test exception")
    }

    @Test
    fun `exception after set fuel - also exception in the command`() {
        // Arrange
        val objMock = mockk<IFuelConsumer>(relaxed = true).also {
            every { it.setFuel(any()) } throws RuntimeException("Test exception")
        }
        val testingCmd = BurnFuelCmd(obj = objMock, value = 1.5)

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