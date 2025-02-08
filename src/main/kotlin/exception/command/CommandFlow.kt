package exception.command

import exception.handler.BaseExceptionHandler
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.onFailure

class CommandFlow( ) : ICommandFlow {

    lateinit var exceptionHandler: BaseExceptionHandler
    private val commandChannel = Channel<ICommand>(Channel.BUFFERED)

    private val flowJob: Job by lazy {
        CoroutineScope(Dispatchers.Default).launch {
            commandChannel.consumeEach { command ->
                try {
                    command.execute()
                } catch (exc: Exception) {
                    exceptionHandler.handle(source = command, exc = exc)
                }
            }
        }
    }

    override fun startFlow() {
        flowJob.start()
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
