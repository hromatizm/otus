package ioc

import command.ICommand
import ioc.Ioc
import generator.CompileStringToClassCmd
import generator.GenerateAdapterCmd
import generator.GetAdapterInstanceCmd
import generator.InterfaceMethodsToStringCmd
import generator.LoadClassFromFileCmd

class GeneratorCmdRegister {

    companion object {

        private val commands = listOf(
            Ioc.Companion.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("InterfaceMethodsToString", { params: Array<out Any> ->
                    InterfaceMethodsToStringCmd(
                        clazz = params[0] as Class<*>
                    )
                })
            ),
            Ioc.Companion.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("CompileStringToClass", { params: Array<out Any> ->
                    CompileStringToClassCmd(
                        sourceCode = params[0] as String,
                        className = params[1] as String,
                        outputDir = params[2] as String
                    )
                })
            ),
            Ioc.Companion.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("GenerateAdapter", { params: Array<out Any> ->
                    GenerateAdapterCmd(
                        interfaceType = params[0] as Class<*>,
                    )
                })
            ),
            Ioc.Companion.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("LoadClassFromFile", { params: Array<out Any> ->
                    LoadClassFromFileCmd(
                        path = params[0] as String,
                        className = params[1] as String
                    )
                })
            ),
            Ioc.Companion.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("Adapter", { params: Array<out Any> ->
                    GetAdapterInstanceCmd(
                        interfaceType = params[0] as Class<*>,
                        obj = params[1] as MutableMap<String, Any>
                    )
                })
            ),
        )
        fun init() {
            commands.forEach { it.execute() }
        }
    }
}