package actor

import command.ICommand
import java.util.concurrent.BlockingDeque

class Actor(
    override val deque: BlockingDeque<ICommand>,
    override val actionAfterStart: () -> Unit,
    val actorName: String,
) : IActor {

    override val threadName = "$actorName-thread"
    override var behavior: () -> Unit
    override var actionAfterStop: () -> Unit = { }

    private val defaultBehavior = {
        val cmd = deque.take()
        try {
            cmd.execute()
        } catch (exc: Exception) {
            println("Exception happened: $exc")
        }
    }.also { behavior = it }

    private var active = true

    private val execFlow = {
        while (active) {
            behavior.invoke()
        }
        actionAfterStop.invoke()
    }

    private val thread = Thread(execFlow).also { it.name = threadName }

    override fun start() {
        thread.start()
        deque.offerFirst(object : ICommand {
            override fun execute() {
                // Заглушка, чтобы поток сразу увидел active == false и завершился
            }
        })
        actionAfterStart.invoke()
    }

    override fun stop() {
        active = false
    }
}
