package exception.log

import org.example.command.ICommand
import exception.command.ICommandFlow
import exception.command.LogExceptionCommand
import exception.handler.DefaultExceptionHandler
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class LogExceptionHandlerTest {

    @Test
    fun `add logCommand to commandFlow when handle`() {
        // Arrange
        val sourceCmdMock = mockk<ICommand>()
        val cmdFlowMock = mockk<ICommandFlow>(relaxed = true)
        val exceptionMock = mockk<Exception>()
        val testingHandler = DefaultExceptionHandler(cmdFlowMock)

        // Act
        testingHandler.handle(source = sourceCmdMock, exc = exceptionMock)

        // Assert
        val logExcCmdSlot = slot<LogExceptionCommand>()
        verify { cmdFlowMock.addCommand(capture(logExcCmdSlot)) }
        assertThat(logExcCmdSlot.captured.exc).isEqualTo(exceptionMock)
        confirmVerified(cmdFlowMock)
    }
}
