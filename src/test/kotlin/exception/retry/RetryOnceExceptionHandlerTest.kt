package exception.retry

import org.example.command.ICommand
import exception.command.ICommandFlow
import exception.command.RetryOnceCommand
import exception.handler.RetryOnceExceptionHandler
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class RetryOnceExceptionHandlerTest {

    @Test
    fun `add logCommand to commandFlow when handle`() {
        // Arrange
        val sourceCmdMock = mockk<ICommand>()
        val cmdFlowMock = mockk<ICommandFlow>(relaxed = true)
        val testingHandler = RetryOnceExceptionHandler(cmdFlowMock)

        // Act
        testingHandler.handle(source = sourceCmdMock, exc = mockk())

        // Assert
        val retryOnceCmdSlot = slot<RetryOnceCommand>()
        verify { cmdFlowMock.addCommand(capture(retryOnceCmdSlot)) }
        assertThat(retryOnceCmdSlot.captured.command).isEqualTo(sourceCmdMock)
        confirmVerified(cmdFlowMock)
    }
}
