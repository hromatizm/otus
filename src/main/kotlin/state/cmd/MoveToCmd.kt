package state.cmd

import command.IValueCommand
import ioc.Ioc
import state.ActorStateHolder
import state.IStatefulActor
import state.IStatefulActorCommand
import state.MoveToState
import java.util.concurrent.BlockingDeque

class MoveToCmd(
    private val actorId: String,
    private val dequeToMove: BlockingDeque<IStatefulActorCommand>,
    private val stateFinalAction: () -> Unit = {}
) : IStatefulActorCommand {

    override fun getNextState(): ActorStateHolder? {
        val actor = Ioc.resolve<IValueCommand<IStatefulActor>>(
            dependencyName = "Актор",
            args = arrayOf(actorId)
        ).execute()
        return ActorStateHolder(
            state = MoveToState(
                actor = actor,
                dequeToMove = dequeToMove,
                finalAction = stateFinalAction,
            )
        )
    }

    override fun execute() {
        // nothing
    }
}