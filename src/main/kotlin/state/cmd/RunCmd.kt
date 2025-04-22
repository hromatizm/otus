package state.cmd

import command.IValueCommand
import ioc.Ioc
import state.ActorStateHolder
import state.DefaultState
import state.IStatefulActor
import state.IStatefulActorCommand

class RunCmd(
    private val actorId: String,
    private val stateFinalAction: () -> Unit = {}
) : IStatefulActorCommand {

    override fun getNextState(): ActorStateHolder? {
        val actor = Ioc.Companion.resolve<IValueCommand<IStatefulActor>>(
            dependencyName = "Актор",
            args = arrayOf(actorId)
        ).execute()
        return ActorStateHolder(
            state = DefaultState(
                actor = actor,
                finalAction = stateFinalAction,
            )
        )
    }

    override fun execute() {
        // nothing
    }
}