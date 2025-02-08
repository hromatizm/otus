package exception.registry

import exception.handler.IExceptionHandler
import kotlin.reflect.KClass

interface IExceptionHandlerRegistry {

    fun <Exc : Exception> registerHandler(sourceClass: Class<*>, exc: Class<Exc>, handler: IExceptionHandler)

}