package exception.handler

import exception.command.ICommand
import exception.command.ICommandFlow
import exception.command.LogExceptionCommand

class DefaultExceptionHandler(
    private val commandFlow: ICommandFlow
) : IExceptionHandler {

    override fun handle(source: ICommand, exc: Exception) {
        val logCmd = LogExceptionCommand(exc = exc)
        commandFlow.addCommand(command = logCmd)
    }
}