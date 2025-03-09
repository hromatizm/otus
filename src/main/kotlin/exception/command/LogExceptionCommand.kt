package exception.command

import exception.logger.ILogger
import exception.logger.Logger
import command.ICommand

class LogExceptionCommand(
    val exc: Exception,
    val logger: ILogger = Logger()
) : ICommand {
    
    override fun execute() {
        logger.log("Exception happened:", exc)
    }
}