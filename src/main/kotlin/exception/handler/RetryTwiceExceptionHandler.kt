package exeption.handler

import exception.command.ICommand
import exception.command.ICommandFlow
import exception.command.RetryTwiceCommand
import exception.handler.IExceptionHandler

class RetryTwiceExceptionHandler(
    private val commandFlow: ICommandFlow
) : IExceptionHandler {

    override fun handle(source: ICommand, exc: Exception) {
        val retryCmd = RetryTwiceCommand(command = source)
        commandFlow.addCommand(command = retryCmd)
    }

}