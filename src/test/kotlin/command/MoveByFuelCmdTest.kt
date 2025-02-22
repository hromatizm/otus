package command

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import motion.move.MoveCmd
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.example.exception.CommandException
import org.example.motion.fuel.BurnFuelCmd
import org.example.motion.fuel.CheckFuelCmd
import org.example.motion.macro.MacroCmd
import org.junit.jupiter.api.Test

class MoveByFuelCmdTest {

    @Test
    fun `move by fuel successfully`() {
        // Arrange
        val checkFuelCmdMock = mockk<CheckFuelCmd>(relaxed = true)
        val burnFuelCmdMock = mockk<BurnFuelCmd>(relaxed = true)
        val moveCmdMock = mockk<MoveCmd>(relaxed = true)
        val testingCmd = MacroCmd(
            commands = listOf(
                checkFuelCmdMock,
                moveCmdMock,
                burnFuelCmdMock,
            )
        )

        // Act
        testingCmd.execute()

        // Assert
        verifySequence {
            checkFuelCmdMock.execute()
            moveCmdMock.execute()
            burnFuelCmdMock.execute()
        }
    }

    @Test
    fun `if fuel not enough - should not move`() {
        // Arrange
        val checkFuelCmdMock = mockk<CheckFuelCmd>().also {
            every { it.execute() } throws CommandException("Not enough fuel")
        }
        val burnFuelCmdMock = mockk<BurnFuelCmd>(relaxed = true)
        val moveCmdMock = mockk<MoveCmd>(relaxed = true)
        val testingCmd = MacroCmd(
            commands = listOf(
                checkFuelCmdMock,
                moveCmdMock,
                burnFuelCmdMock,
            )
        )

        // Act
        val exception = catchThrowable {
            testingCmd.execute()
        }

        // Assert
        assertThat(exception)
            .isExactlyInstanceOf(CommandException::class.java)
            .hasMessage("Not enough fuel")
        verify { checkFuelCmdMock.execute() }
        verify(exactly = 0) { moveCmdMock.execute() }
        verify(exactly = 0) { burnFuelCmdMock.execute() }
    }

    @Test
    fun `if can not move - should not burn fuel`() {
        // Arrange
        val checkFuelCmdMock = mockk<CheckFuelCmd>(relaxed = true)
        val moveCmdMock = mockk<MoveCmd>().also {
            every { it.execute() } throws CommandException("Move failed")
        }
        val burnFuelCmdMock = mockk<BurnFuelCmd>(relaxed = true)

        val testingCmd = MacroCmd(
            commands = listOf(
                checkFuelCmdMock,
                moveCmdMock,
                burnFuelCmdMock,
            )
        )

        // Act
        val exception = catchThrowable {
            testingCmd.execute()
        }

        // Assert
        assertThat(exception)
            .isExactlyInstanceOf(CommandException::class.java)
            .hasMessage("Move failed")
        verifySequence {
            checkFuelCmdMock.execute()
            moveCmdMock.execute()
        }
        verify(exactly = 0) { burnFuelCmdMock.execute() }
    }
}
