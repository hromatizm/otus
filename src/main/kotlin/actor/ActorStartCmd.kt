package actor

import command.ICommand
import java.util.concurrent.BlockingDeque

class ActorStartCmd(
    private val deque: BlockingDeque<ICommand>,
    private val actorName: String,
    private var actionAfterStart: () -> Unit = {}
) : ICommand {

    override fun execute() {
        val actor = Actor(deque = deque, actorName = actorName, actionAfterStart = actionAfterStart)
        ActorRegistry.register(actorName = actorName, actor = actor)
        actor.start()
    }
}