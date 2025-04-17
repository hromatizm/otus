package spring.event.loop

import command.ICommand
import java.util.concurrent.BlockingDeque

class GameLoop(
    override val gameId: String,
    private val deque: BlockingDeque<ICommand>,
) : IGameLoop {

    private var active = true

    private val eventLoop = {
        while (active) {
            val cmd = deque.take()
            try {
                cmd.execute()
            } catch (exc: Exception) {
                println("Exception happened: $exc")
            }
        }
    }

    private val thread = Thread(eventLoop).also { it.name = "Thread-for-game-$gameId" }

    override fun start() {
        thread.start()
    }

    override fun stop() {
        active = false
    }

    override fun addEvent(command: ICommand) {
        deque.add(command)
    }
}
