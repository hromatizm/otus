package state.cmd

import command.IValueCommand
import spring.ActorId
import state.IStatefulActor

class GetStatefulActorCmd(
    private val actorId: String,
    private val actorMap: Map<ActorId, IStatefulActor>,
) : IValueCommand<IStatefulActor> {

    override fun execute(): IStatefulActor {
        return actorMap[ActorId(actorId)] ?: throw RuntimeException("Актор с id=$actorId не найден")
    }
}