package actor

import command.ICommand
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.LinkedBlockingDeque

class ActorTest {

    @Test
    fun `after actor start thread is alive`() {
        // Arrange
        val latch = CountDownLatch(1)
        val commandDeque = LinkedBlockingDeque<ICommand>()
        val actorName = "actorName"
        val testingCmd = ActorStartCmd(
            deque = commandDeque,
            actorName = actorName,
            actionAfterStart = { latch.countDown() }
        )

        // Act
        testingCmd.execute()
        val actor = ActorRegistry.get(actorName)
        val thread = Thread.getAllStackTraces().keys.firstOrNull { it.name == actor?.threadName }

        // Assert
        latch.await()
        assertThat(thread?.isAlive).isTrue
    }

    @Test
    fun `after actor start command was executed`() {
        // Arrange
        val commandDeque = LinkedBlockingDeque<ICommand>()
        val actorName = "actorName"
        val latch = CountDownLatch(1)
        val mockCmdToExec = mockk<ICommand>(relaxed = true).also {
            every { it.execute() } answers { latch.countDown() }
        }
        val testingCmd = ActorStartCmd(
            deque = commandDeque,
            actorName = actorName,
        )

        // Act
        testingCmd.execute()
        commandDeque.add(mockCmdToExec)

        // Assert
        latch.await()
        verify { mockCmdToExec.execute() }
    }

    @Test
    fun `after hard stop no command was executed`() {
        // Arrange
        val commandDeque = LinkedBlockingDeque<ICommand>()
        val actorName = "actorName"
        ActorStartCmd(
            deque = commandDeque,
            actorName = actorName,
        ).execute()
        val actor = ActorRegistry.get(actorName)
        val latch = CountDownLatch(1)
        actor?.actionAfterStop = { latch.countDown() }

        val mockCmdBeforeStop = mockk<ICommand>(relaxed = true)
        val mockCmdAfterStop = mockk<ICommand>(relaxed = true)
        val testingCmd = ActorHardStopCmd(actorName = actorName)

        // Act
        with(commandDeque) {
            add(mockCmdBeforeStop)
            add(testingCmd)
            add(mockCmdAfterStop)
        }

        // Assert
        latch.await()
        verify { mockCmdBeforeStop.execute() }
        verify(exactly = 0) { mockCmdAfterStop.execute() }
    }
}
