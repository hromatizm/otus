package exception.log


import exception.command.LogExceptionCommand
import exception.logger.Logger
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class LogExceptionCommandTest {

    @Test
    fun `log exception when command executed`() {
        // Arrange
        val exceptionMock = mockk<Exception>()
        val loggerMock = mockk<Logger>(relaxed = true)
        val testingCmd = LogExceptionCommand(exceptionMock, loggerMock)

        // Act
        testingCmd.execute()

        // Assert
        verify { loggerMock.log("Exception happened:", exceptionMock) }
        confirmVerified(loggerMock)
    }
}
