package state

class DefaultState(
    private val actor: IStatefulActor,
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
            cmd.execute()
            finalAction.invoke()
        } catch (exc: Exception) {
            println("Exception happened: $exc")
        }
        return nextState
    }
}