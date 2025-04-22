package state.cmd

import command.ICommand
import spring.ActorId
import state.IStatefulActor

class RegisterStatefulActorCmd(
    private val actor: IStatefulActor,
    private val actorMap: MutableMap<ActorId, IStatefulActor>,
) : ICommand {

    override fun execute() {
        actorMap[actor.id] = actor
    }
}