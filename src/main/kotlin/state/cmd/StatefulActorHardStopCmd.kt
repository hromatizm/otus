package state.cmd

import state.ActorStateHolder
import state.IStatefulActorCommand

class StatefulActorHardStopCmd() : IStatefulActorCommand {

    override fun getNextState(): ActorStateHolder? {
        return ActorStateHolder(state = null)
    }

    override fun execute() {
        // nothing
    }
}