package state

import java.util.concurrent.BlockingDeque

class MoveToState(
    private val actor: IStatefulActor,
    private val dequeToMove: BlockingDeque<IStatefulActorCommand>,
    private val finalAction: () -> Unit = {}
) : IActorState {

    override fun handle(): IActorState? {
        var nextState: IActorState? = this
        val cmd = actor.deque.take()
        try {
            val stateHolder = cmd.getNextState()
            stateHolder?.let {
                nextState = it.state
            }
            dequeToMove.add(cmd)
            finalAction.invoke()
        } catch (exc: Exception) {
            println("Exception happened: $exc")
        }
        return nextState
    }
}
