package state

import spring.ActorId
import java.util.concurrent.BlockingDeque

class StatefulActor(
    override val id: ActorId,
    override val deque: BlockingDeque<IStatefulActorCommand>,
    override val actionAfterStart: () -> Unit,
    ) : IStatefulActor {

    override val threadName = "$id-thread"
    override var actionAfterStop: () -> Unit = { }
    override var state: IActorState? = DefaultState(actor = this)

    private var active = true

    private val execFlow = {
        while (active) {
            state = state?.handle()
            state ?: stop()
        }
        actionAfterStop.invoke()
        threadInterrupt()
    }

    private val thread = Thread(execFlow).also { it.name = threadName }

    override fun start() {
        thread.start()
        actionAfterStart.invoke()
    }

    override fun stop() {
        active = false
    }

    private fun threadInterrupt() {
       thread.interrupt()
    }
}