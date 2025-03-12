package org.example.generator

import command.IValueCommand
import java.lang.reflect.Method

class InterfaceMethodsToStringCmd(
    private val clazz: Class<*>,
) : IValueCommand<String> {

    private val methodPrefixes = listOf("get", "set")

    override fun execute(): String {
        return clazz.methods.joinToString("\n\n") { implement(it) }
    }

    private fun implement(method: Method): String {
        val methodType = getMethodType(method)
        val result = when (methodType) {
            "get" -> implementGetter(method)
            "set" -> implementSetter(method)
            else -> implementCommandMethod(method)
        }
        return result
    }

    private fun getMethodType(method: Method): String {
        return methodPrefixes.firstOrNull { method.name.startsWith(it) } ?: ""
    }

    private fun implementGetter(method: Method): String {
        val paramName = method.name.removePrefix("get").lowercase()
        val returnType = method.returnType.name
        return """
        override fun ${method.name}(): $returnType {
            return ioc.Ioc.resolve<command.IValueCommand<$returnType>>(
                dependencyName = "${clazz.name}:$paramName.get",
                args = arrayOf(obj),
            ).execute()
        }""".trimIndent()
    }

    private fun implementSetter(method: Method): String {
        val paramName = method.name.removePrefix("set").lowercase()
        val methodArg = method.parameters.first()
        return """
        override fun ${method.name}(newValue: ${methodArg.type.name}) {
            return ioc.Ioc.resolve<command.ICommand>(
                dependencyName = "${clazz.name}:$paramName.set",
                args = arrayOf(obj, newValue),
            ).execute()
        }""".trimIndent()
    }

    private fun implementCommandMethod(method: Method): String {
        return """
        override fun ${method.name}() {
            return ioc.Ioc.resolve<command.ICommand>(
                dependencyName = "${clazz.name}:${method.name}",
                args = arrayOf(obj),
            ).execute()
        }""".trimIndent()
    }
}