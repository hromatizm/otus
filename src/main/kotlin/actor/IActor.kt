package actor

import command.ICommand
import java.util.concurrent.BlockingDeque

interface IActor {

    val deque:  BlockingDeque<ICommand>
    val threadName: String
    var behavior: () -> Unit
    val actionAfterStart: () -> Unit
    var actionAfterStop: () -> Unit

    fun start()

    fun stop()
}
