package state.cmd

import command.ICommand
import ioc.Ioc
import spring.ActorId
import state.IStatefulActorCommand
import state.StatefulActor
import java.util.concurrent.BlockingDeque

class StatefulActorStartCmd(
    private val actorId: String,
    private val deque: BlockingDeque<IStatefulActorCommand>,
    private var actionAfterStart: () -> Unit = {}
) : ICommand {

    override fun execute() {
        val actor = StatefulActor(id = ActorId(actorId), deque = deque,  actionAfterStart = actionAfterStart)
        Ioc.resolve<ICommand>(
            dependencyName = "Добавить актор",
            args = arrayOf(actor)
        ).execute()
        actor.start()
    }
}