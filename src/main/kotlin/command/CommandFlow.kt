package command

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.onFailure
import org.example.command.ICommandFlow

class CommandFlow() : ICommandFlow {

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    private val commandChannel = Channel<ICommand>(Channel.BUFFERED)
    private lateinit var flowJob: Job

    override fun startFlow() {
        flowJob = scope.launch {
            commandChannel.consumeEach { command ->
                try {
                    command.execute()
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
    }

    override fun addCommand(command: ICommand) {
        commandChannel.trySend(command)
            .onFailure {
                println("Не удалось добавить команду в канал: ${it?.message}")
            }
    }

    override fun stopFlow() {
        commandChannel.close()
        flowJob.cancel()
    }
}
