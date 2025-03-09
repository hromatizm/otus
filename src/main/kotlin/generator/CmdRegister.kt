package org.example.generator

import command.ICommand
import ioc.Ioc
import org.example.motion.move.SetLocationCmd

class CmdRegister {

    companion object {

        private val commands = listOf(
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("InterfaceMethodsToString", { params: Array<out Any> ->
                    InterfaceMethodsToStringCmd(
                        clazz = params[0] as Class<*>
                    )
                })
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("CompileStringToClass", { params: Array<out Any> ->
                    CompileStringToClassCmd(
                        sourceCode = params[0] as String,
                        className = params[1] as String,
                        outputDir = params[2] as String
                    )
                })
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("GenerateAdapter", { params: Array<out Any> ->
                    GenerateAdapterCmd(
                        interfaceType = params[0] as Class<*>,
                    )
                })
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("LoadClassFromFile", { params: Array<out Any> ->
                    LoadClassFromFileCmd(
                        path = params[0] as String,
                        className = params[1] as String
                    )
                })
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("Adapter", { params: Array<out Any> ->
                    GetAdapterInstanceCmd(
                        interfaceType = params[0] as Class<*>,
                        obj = params[1] as MutableMap<String, Any>
                    )
                })
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("motion.move.IMoveable:location.set", { params: Array<out Any> ->
                    SetLocationCmd(
                        obj = params[0] as MutableMap<String, Any>,
                        newValue = params[1]
                    )
                })
            )
        )

        fun init() {
            commands.forEach { it.execute() }
        }
    }
}