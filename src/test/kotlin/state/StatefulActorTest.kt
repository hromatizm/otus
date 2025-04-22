package state

import command.ICommand
import command.IValueCommand
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import ioc.Ioc
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import state.cmd.MoveToCmd
import state.cmd.RunCmd
import state.cmd.StatefulActorHardStopCmd
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.LinkedBlockingDeque

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class StatefulActorTest {

    @BeforeEach
    fun initIoc() {
        Ioc.initIoc()
    }

    @Test
    fun `after actor start thread is alive`() {
        // Arrange
        val actorId = UUID.randomUUID().toString()
        val commandDeque = LinkedBlockingDeque<IStatefulActorCommand>()
        val latch = CountDownLatch(1)
        val testingCmd = Ioc.resolve<ICommand>(
            dependencyName = "Запустить актор",
            args = arrayOf(actorId, commandDeque, { latch.countDown() })
        )

        // Act
        testingCmd.execute()
        val actor = Ioc.resolve<IValueCommand<StatefulActor>>(
            dependencyName = "Актор",
            args = arrayOf(actorId)
        ).execute()
        val thread = actor.getThread()

        // Assert
        latch.await()
        assertThat(thread?.isAlive).isTrue
    }

    @Test
    fun `after command is added it is executed`() {
        // Arrange
        val actorId = UUID.randomUUID().toString()
        val commandDeque = LinkedBlockingDeque<IStatefulActorCommand>()
        val latch = CountDownLatch(1)
        val mockCmdToExec = mockk<IStatefulActorCommand>(relaxed = true).also {
            every { it.execute() } answers { latch.countDown() }
        }
        val testingCmd = Ioc.resolve<ICommand>(
            dependencyName = "Запустить актор",
            args = arrayOf(actorId, commandDeque, { })
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
        val actorId = UUID.randomUUID().toString()
        val commandDeque = LinkedBlockingDeque<IStatefulActorCommand>()
        Ioc.resolve<ICommand>(
            dependencyName = "Запустить актор",
            args = arrayOf(actorId, commandDeque, { })
        ).execute()

        val actor = Ioc.resolve<IValueCommand<StatefulActor>>(
            dependencyName = "Актор",
            args = arrayOf(actorId)
        ).execute()
        val latch = CountDownLatch(1)
        actor.actionAfterStop = { latch.countDown() }

        val mockCmdBeforeStop = mockk<IStatefulActorCommand>(relaxed = true).also {
            every { it.getNextState() }.returns(null)
        }
        val mockCmdAfterStop = mockk<IStatefulActorCommand>(relaxed = true).also {
            every { it.getNextState() }.returns(null)
        }
        val testingCmd = StatefulActorHardStopCmd()

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
    fun `after moveToCommand actor state was changed`() {
        // Arrange
        val actorId = UUID.randomUUID().toString()
        val commandDeque1 = LinkedBlockingDeque<IStatefulActorCommand>()
        Ioc.resolve<ICommand>(
            dependencyName = "Запустить актор",
            args = arrayOf(actorId, commandDeque1, { })
        ).execute()

        val commandDeque2 = LinkedBlockingDeque<IStatefulActorCommand>()
        val latch = CountDownLatch(1)
        val testingCmd = MoveToCmd(
            actorId = actorId,
            dequeToMove = commandDeque2,
            stateFinalAction = { latch.countDown() }
        )
        val mockCmdToMove = mockk<IStatefulActorCommand>(relaxed = true).also {
            every { it.getNextState() }.returns(null)
        }

        // Act
        with(commandDeque1) {
            add(testingCmd)
            add(mockCmdToMove)
        }

        // Assert
        latch.await()
        val actor = Ioc.resolve<IValueCommand<StatefulActor>>(
            dependencyName = "Актор",
            args = arrayOf(actorId)
        ).execute()
        assertThat(actor.state).isInstanceOf(MoveToState::class.java)
    }

    @Test
    fun `after moveToCommand commands moving to new queue`() {
        // Arrange
        val actorId = UUID.randomUUID().toString()
        val commandDeque1 = LinkedBlockingDeque<IStatefulActorCommand>()
        Ioc.resolve<ICommand>(
            dependencyName = "Запустить актор",
            args = arrayOf(actorId, commandDeque1, { })
        ).execute()

        val commandDeque2 = LinkedBlockingDeque<IStatefulActorCommand>()
        val latch = CountDownLatch(1)
        val testingCmd = MoveToCmd(
            actorId = actorId,
            dequeToMove = commandDeque2,
            stateFinalAction = { latch.countDown() }
        )
        val mockCmdToMove = mockk<IStatefulActorCommand>(relaxed = true).also {
            every { it.getNextState() }.returns(null)
        }

        // Act
        with(commandDeque1) {
            add(testingCmd)
            add(mockCmdToMove)
        }

        // Assert
        latch.await()
        assertThat(commandDeque1).isEmpty()
        assertThat(commandDeque2).containsExactly(mockCmdToMove)
    }

    @Test
    fun `after runCmd actor MoveToState was changed to default and command was executed`() {
        // Arrange
        val actorId = UUID.randomUUID().toString()
        val commandDeque1 = LinkedBlockingDeque<IStatefulActorCommand>()
        Ioc.resolve<ICommand>(
            dependencyName = "Запустить актор",
            args = arrayOf(actorId, commandDeque1, { })
        ).execute()

        val commandDeque2 = LinkedBlockingDeque<IStatefulActorCommand>()
        val latch = CountDownLatch(1)
        val moveToCmd = MoveToCmd(
            actorId = actorId,
            dequeToMove = commandDeque2,
            stateFinalAction = { latch.countDown() }
        )
        val testingCmd = RunCmd(
            actorId = actorId,
            stateFinalAction = { latch.countDown() }
        )
        val mockCmdForDefaultHandle = mockk<IStatefulActorCommand>(relaxed = true).also {
            every { it.getNextState() }.returns(null)
        }

        // Act
        with(commandDeque1) {
            add(moveToCmd)
            add(testingCmd)
            add(mockCmdForDefaultHandle)
        }

        // Assert
        latch.await()
        val actor = Ioc.resolve<IValueCommand<StatefulActor>>(
            dependencyName = "Актор",
            args = arrayOf(actorId)
        ).execute()
        assertThat(actor.state).isInstanceOf(DefaultState::class.java)
        verify { mockCmdForDefaultHandle.execute() }
    }


    private fun StatefulActor.getThread(): Thread? {
        return Thread.getAllStackTraces().keys.firstOrNull { it.name == this.threadName }
    }
}
