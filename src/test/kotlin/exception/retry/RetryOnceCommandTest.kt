package exception.retry

import exception.command.ICommand
import exception.command.RetryOnceCommand
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.TestInstance
import java.sql.SQLException

import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RetryOnceCommandTest {

    @Test
    fun `execute source command when retry command executed`() {
        // Arrange
        val sourceCmdMock = mockk<ICommand>(relaxed = true)
        val testingCmd = RetryOnceCommand(sourceCmdMock)

        // Act
        testingCmd.execute()

        // Assert
        verify { sourceCmdMock.execute() }
        confirmVerified(sourceCmdMock)
    }

    @Test
    fun `throw exception when source command throws`() {
        // Arrange
        val testExcMsg = "Test exception message"
        val sourceCmdMock = mockk<ICommand>(relaxed = true).also {
            every { it.execute() } throws SQLException(testExcMsg)
        }
        val testingCmd = RetryOnceCommand(sourceCmdMock)

        // Act
        val exception = catchThrowable {
            testingCmd.execute()
        }

        // Assert
        verify { sourceCmdMock.execute() }
        assertThat(exception)
            .isExactlyInstanceOf(SQLException::class.java)
            .hasMessage(testExcMsg)
        confirmVerified(sourceCmdMock)
    }
}
