package state

interface IStatefulActorCommand {

    fun getNextState(): ActorStateHolder?

    fun execute()
}