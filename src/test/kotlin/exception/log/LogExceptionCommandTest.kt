package exception.log

import exception.command.LogExceptionCommand
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.TestInstance
import java.util.logging.Logger

import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LogExceptionCommandTest {

    @Test
    fun `log exception when command executed`() {
        // Arrange
        val exceptionMock = mockk<Exception>()
        val loggerMock = mockk<Logger>(relaxed = true)
        mockkStatic(Logger::class)
        every { Logger.getLogger(any()) } returns loggerMock
        val testingCmd = LogExceptionCommand(exceptionMock)

        // Act
        testingCmd.execute()

        // Assert
        verify { loggerMock.info("Exception happened: $exceptionMock") }
        confirmVerified(loggerMock)
    }
}
