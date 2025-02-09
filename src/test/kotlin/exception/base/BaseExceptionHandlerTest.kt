package exception.base

import exception.command.ICommandFlow
import exception.command.LogExceptionCommand
import exception.command.RetryOnceCommand
import exception.command.RetryTwiceCommand
import exception.handler.BaseExceptionHandler
import exception.handler.DefaultExceptionHandler
import exception.handler.IExceptionHandler
import exception.handler.RetryOnceExceptionHandler
import exeption.handler.RetryTwiceExceptionHandler
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import motion.move.Move
import org.junit.jupiter.api.TestInstance
import java.sql.SQLException
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class BaseExceptionHandlerTest {

    @Test
    fun `first retry than log`() {
        // Arrange
        val sqlExcMock = mockk<SQLException>()
        val cmdFlowMock = mockk<ICommandFlow>(relaxed = true)
        val moveCmdMock = mockk<Move>()
        val retryOnceCmdMock = mockk<RetryOnceCommand>()
        val retryOnceExcHandlerMock = mockk<RetryOnceExceptionHandler>()
        val logExcCmdMock = mockk<LogExceptionCommand>(relaxed = true)
        val logExcHandlerMock = mockk<DefaultExceptionHandler>()
        val handlersRegistryMock =
            mockk<MutableMap<Class<*>, MutableMap<Class<out Exception>, IExceptionHandler>>>(relaxed = true)
        val testingHandler = BaseExceptionHandler(cmdFlowMock, handlersRegistryMock)
        every { cmdFlowMock.addCommand(moveCmdMock) } answers {
            moveCmdMock.execute()
        }
        every { moveCmdMock.execute() } answers {
            testingHandler.handle(source = moveCmdMock, exc = sqlExcMock)
        }
        every { handlersRegistryMock[Move::class.java] } returns mutableMapOf(
            SQLException::class.java to retryOnceExcHandlerMock
        )
        every { retryOnceExcHandlerMock.handle(moveCmdMock, sqlExcMock) } answers {
            cmdFlowMock.addCommand(retryOnceCmdMock)
        }
        every { cmdFlowMock.addCommand(retryOnceCmdMock) } answers {
            retryOnceCmdMock.execute()
        }
        every { retryOnceCmdMock.execute() } answers {
            testingHandler.handle(source = retryOnceCmdMock, exc = sqlExcMock)
        }
        every { handlersRegistryMock[RetryOnceCommand::class.java] } returns mutableMapOf(
            SQLException::class.java to logExcHandlerMock,
        )
        every { logExcHandlerMock.handle(retryOnceCmdMock, sqlExcMock) } answers {
            cmdFlowMock.addCommand(logExcCmdMock)
        }
        every { cmdFlowMock.addCommand(logExcCmdMock) } answers {
            logExcCmdMock.execute()
        }

        // Act
        cmdFlowMock.addCommand(moveCmdMock)

        // Assert
        verify { retryOnceExcHandlerMock.handle(moveCmdMock, sqlExcMock) }
        verify { retryOnceCmdMock.execute() }
        verify { logExcHandlerMock.handle(retryOnceCmdMock, sqlExcMock) }
        verify { logExcCmdMock.execute() }
        confirmVerified(retryOnceExcHandlerMock)
        confirmVerified(logExcHandlerMock)
        confirmVerified(logExcCmdMock)
    }

    @Test
    fun `first retry than retry than log`() {
        // Arrange
        val sqlExcMock = mockk<SQLException>()
        val cmdFlowMock = mockk<ICommandFlow>(relaxed = true)
        val moveCmdMock = mockk<Move>()
        val retryOnceCmdMock = mockk<RetryOnceCommand>()
        val retryOnceExcHandlerMock = mockk<RetryOnceExceptionHandler>()
        val retryTwiceCmdMock = mockk<RetryTwiceCommand>()
        val retryTwiceExcHandlerMock = mockk<RetryTwiceExceptionHandler>()
        val logExcCmdMock = mockk<LogExceptionCommand>(relaxed = true)
        val logExcHandlerMock = mockk<DefaultExceptionHandler>()
        val handlersRegistryMock =
            mockk<MutableMap<Class<*>, MutableMap<Class<out Exception>, IExceptionHandler>>>(relaxed = true)
        val testingHandler = BaseExceptionHandler(cmdFlowMock, handlersRegistryMock)
        every { cmdFlowMock.addCommand(moveCmdMock) } answers {
            moveCmdMock.execute()
        }
        every { moveCmdMock.execute() } answers {
            testingHandler.handle(source = moveCmdMock, exc = sqlExcMock)
        }
        every { handlersRegistryMock[Move::class.java] } returns mutableMapOf(
            SQLException::class.java to retryOnceExcHandlerMock
        )
        every { retryOnceExcHandlerMock.handle(moveCmdMock, sqlExcMock) } answers {
            cmdFlowMock.addCommand(retryOnceCmdMock)
        }
        every { cmdFlowMock.addCommand(retryOnceCmdMock) } answers {
            retryOnceCmdMock.execute()
        }
        every { retryOnceCmdMock.execute() } answers {
            testingHandler.handle(source = retryOnceCmdMock, exc = sqlExcMock)
        }
        every { handlersRegistryMock[RetryOnceCommand::class.java] } returns mutableMapOf(
            SQLException::class.java to retryTwiceExcHandlerMock,
        )
        every { retryTwiceExcHandlerMock.handle(retryOnceCmdMock, sqlExcMock) } answers {
            cmdFlowMock.addCommand(retryTwiceCmdMock)
        }
        every { cmdFlowMock.addCommand(retryTwiceCmdMock) } answers {
            retryTwiceCmdMock.execute()
        }
        every { retryTwiceCmdMock.execute() } answers {
            testingHandler.handle(source = retryTwiceCmdMock, exc = sqlExcMock)
        }
        every { handlersRegistryMock[RetryTwiceCommand::class.java] } returns mutableMapOf(
            SQLException::class.java to logExcHandlerMock,
        )
        every { logExcHandlerMock.handle(retryTwiceCmdMock, sqlExcMock) } answers {
            cmdFlowMock.addCommand(logExcCmdMock)
        }
        every { cmdFlowMock.addCommand(logExcCmdMock) } answers {
            logExcCmdMock.execute()
        }

        // Act
        cmdFlowMock.addCommand(moveCmdMock)

        // Assert
        verify { retryOnceExcHandlerMock.handle(moveCmdMock, sqlExcMock) }
        verify { retryOnceCmdMock.execute() }
        verify { retryTwiceExcHandlerMock.handle(retryOnceCmdMock, sqlExcMock) }
        verify { retryTwiceCmdMock.execute() }
        verify { logExcHandlerMock.handle(retryTwiceCmdMock, sqlExcMock) }
        verify { logExcCmdMock.execute() }
    }
}
