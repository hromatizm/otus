package exception.registry

import exception.command.ICommandFlow
import exception.command.RetryOnceCommand
import exception.handler.RetryOnceExceptionHandler
import exeption.handler.RetryTwiceExceptionHandler
import motion.move.Move
import org.example.PrintACommand
import java.io.IOException
import java.sql.SQLException

class DefaultExceptionHandlerRegistryFillService(
    private val registry: IExceptionHandlerRegistry,
    private val commandFlow: ICommandFlow,
) : IExceptionHandlerRegistryFillService {

    override fun fillRegistry() {
        with(registry) {
            registerHandler(
                sourceClass = Move::class.java,
                exc = SQLException::class.java,
                handler = RetryOnceExceptionHandler(commandFlow = commandFlow)
            )
            registerHandler(
                sourceClass = Move::class.java,
                exc = IOException::class.java,
                handler = RetryOnceExceptionHandler(commandFlow = commandFlow)
            )
            registerHandler(
                sourceClass = RetryOnceCommand::class.java,
                exc = SQLException::class.java,
                handler = RetryTwiceExceptionHandler(commandFlow = commandFlow)
            )
            registerHandler(
                sourceClass = RetryOnceCommand::class.java,
                exc = IOException::class.java,
                handler = RetryTwiceExceptionHandler(commandFlow = commandFlow)
            )
            registerHandler(
                sourceClass = PrintACommand::class.java,
                exc = RuntimeException::class.java,
                handler = RetryOnceExceptionHandler(commandFlow = commandFlow)
            )
            registerHandler(
                sourceClass = RetryOnceCommand::class.java,
                exc = RuntimeException::class.java,
                handler = RetryTwiceExceptionHandler(commandFlow = commandFlow)
            )
        }
    }
}