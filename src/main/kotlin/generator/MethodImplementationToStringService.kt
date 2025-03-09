package org.example.generator

import java.lang.reflect.Method

class MethodImplementationToStringService {

    private val methodPrefixes = listOf("get", "set")

    fun implementAll(clazz: Class<*>): String {
        return clazz.methods.joinToString("\n\n") { implement(clazz, it) }
    }

    private fun implement(clazz: Class<*>, method: Method): String {
        val methodType = getMethodType(method)
        val result = when (methodType) {
            "get" -> implementGetter(clazz, method)
            "set" -> implementSetter(clazz, method)
            else -> implementOther(clazz, method)
        }
        return result
    }

    private fun getMethodType(method: Method): String {
        return methodPrefixes.firstOrNull { method.name.startsWith(it) } ?: ""
    }

    private fun implementGetter(clazz: Class<*>, method: Method): String {
        val paramName = method.name.removePrefix("get").lowercase()
        val returnType = method.returnType.name
        return """
        override fun ${method.name}(): $returnType {
            return ioc.Ioc.resolve<$returnType>(
                dependencyName = "${clazz.name}:$paramName.get",
                args = arrayOf(obj),
            )
        }""".trimIndent()
    }

    private fun implementSetter(clazz: Class<*>, method: Method): String {
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

    private fun implementOther(clazz: Class<*>, method: Method): String {
        return ""
    }
}