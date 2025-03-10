package exception.handler

import command.ICommand
import exception.command.ICommandFlow
import exception.command.RetryOnceCommand

class RetryOnceExceptionHandler(
    private val commandFlow: ICommandFlow
) : IExceptionHandler {

    override fun handle(source: ICommand, exc: Exception) {
        val retryCmd = RetryOnceCommand(command = source)
        commandFlow.addCommand(command = retryCmd)
    }
}
