package state

import spring.ActorId
import java.util.concurrent.BlockingDeque

interface IStatefulActor {

    val id: ActorId
    val deque: BlockingDeque<IStatefulActorCommand>
    val threadName: String
    val actionAfterStart: () -> Unit
    var actionAfterStop: () -> Unit
    var state: IActorState?

    fun start()

    fun stop()
}