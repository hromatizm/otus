package exception.command

import java.util.logging.Logger

class LogExceptionCommand(
   val exc: Exception,
) : ICommand {

    companion object {
        val logger: Logger = Logger.getLogger(LogExceptionCommand::class.java.name)
    }

    override fun execute() {
        logger.info("Exception happened: $exc")
    }
}