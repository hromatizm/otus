package exception.command

import org.slf4j.LoggerFactory


class LogExceptionCommand(
    private val exc: Exception
) : ICommand {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute() {
        logger.error("Exception happened.", exc)
    }
}