package state

interface IActorState {

    fun handle(): IActorState?
}