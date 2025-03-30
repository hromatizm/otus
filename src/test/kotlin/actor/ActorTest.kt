package actor

import command.ICommand
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import actor.ActorSoftStopCmd
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.LinkedBlockingDeque

class ActorTest {

    @Test
    fun `after actor start thread is alive`() {
        // Arrange
        val commandDeque = LinkedBlockingDeque<ICommand>()
        val actorName = "actorName"
        val latch = CountDownLatch(1)
        val testingCmd = ActorStartCmd(
            deque = commandDeque,
            actorName = actorName,
            actionAfterStart = { latch.countDown() }
        )

        // Act
        testingCmd.execute()
        val actor = ActorRegistry.get(actorName)
        val thread = actor?.getThread()

        // Assert
        latch.await()
        assertThat(thread?.isAlive).isTrue
    }

    @Test
    fun `after command is added it is executed`() {
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

    @Test
    fun `after soft stop existing commands were executed`() {
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
        val testingCmd = ActorSoftStopCmd(actorName = actorName)

        // Act
        with(commandDeque) {
            add(mockCmdBeforeStop)
            add(testingCmd)
            add(mockCmdAfterStop)
        }

        // Assert
        latch.await()
        verify { mockCmdBeforeStop.execute() }
        verify { mockCmdAfterStop.execute() }
    }

    @Test
    fun `after soft stop actor stopped`() {
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
        val testingCmd = ActorSoftStopCmd(actorName = actorName)

        // Act
        commandDeque.add(testingCmd)

        // Assert
        latch.await()
        assertThat(latch.count).isEqualTo(0)
    }

    private fun IActor.getThread(): Thread? {
        return Thread.getAllStackTraces().keys.firstOrNull { it.name == this.threadName }
    }
}
