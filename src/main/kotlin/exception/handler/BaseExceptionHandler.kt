package exception.handler

import exception.command.ICommand
import exception.command.ICommandFlow
import exception.registry.IExceptionHandlerRegistry

class BaseExceptionHandler(
    private val commandFlow: ICommandFlow,
) : IExceptionHandlerRegistry, IExceptionHandler {

    private val handlersRegistry = mutableMapOf<Class<*>, MutableMap<Class<out Exception>, IExceptionHandler>>()
    private val universalHandler = DefaultExceptionHandler(commandFlow = commandFlow)

    override fun <Exc : Exception>registerHandler(sourceClass: Class<*>, excClass: Class<Exc>, handler: IExceptionHandler) {
        val handlersMap = handlersRegistry[sourceClass]
        if (handlersMap == null) {
            handlersRegistry[sourceClass] = mutableMapOf(excClass to handler)
        } else {
            handlersMap[excClass] = handler
        }
    }

    override fun handle(source: ICommand, exc: Exception) {
        val handler = getHandler(sourceClass = source::class.java, excClass = exc::class.java)
        handler.handle(source, exc)
    }

    private fun getHandler(sourceClass: Class<*>, excClass: Class<*>): IExceptionHandler {
        return handlersRegistry[sourceClass]?.get(excClass) ?: universalHandler
    }
}