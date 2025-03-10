package org.example.generator

import command.ICommand
import command.IValueCommand
import ioc.Ioc

class GenerateAdapterCmd(
    private val interfaceType: Class<*>
) : ICommand {

    override fun execute() {
        val methodsStr = Ioc.resolve<IValueCommand<String>>(
            dependencyName = "InterfaceMethodsToString",
            args = arrayOf(interfaceType)
        ).execute()

        val adapterName = "Generated${interfaceType.simpleName.drop(1)}Adapter"

        val classStr = """
    class $adapterName(
        val obj: MutableMap<String, Any>
    ) : ${interfaceType.name} {
    
${methodsStr.prependIndent("        ")}
    }
    """.trimIndent()

        Ioc.resolve<ICommand>(
            dependencyName = "CompileStringToClass",
            args = arrayOf(classStr, adapterName, "build")
        ).execute()
    }
}
