package org.example.ioc

import command.FinishCmd
import command.ICommand
import ioc.Ioc
import motion.move.GetLocationCmd
import motion.move.GetVelocityCmd
import motion.move.SetLocationCmd

class MoveCmdRegister {
    companion object {

        private val commands = listOf(
            // IMoveable
            Ioc.Companion.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("motion.move.IMoveable:velocity.get", { params: Array<out Any> ->
                    GetVelocityCmd(
                        obj = params[0] as MutableMap<String, Any>,
                    )
                })
            ),
            Ioc.Companion.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("motion.move.IMoveable:location.get", { params: Array<out Any> ->
                    GetLocationCmd(
                        obj = params[0] as MutableMap<String, Any>,
                    )
                })
            ),
            Ioc.Companion.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("motion.move.IMoveable:location.set", { params: Array<out Any> ->
                    SetLocationCmd(
                        obj = params[0] as MutableMap<String, Any>,
                        newValue = params[1]
                    )
                })
            ),
            // IExtendedMoveable
            Ioc.Companion.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("motion.move.IExtendedMoveable:velocity.get", { params: Array<out Any> ->
                    GetVelocityCmd(
                        obj = params[0] as MutableMap<String, Any>,
                    )
                })
            ),
            Ioc.Companion.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("motion.move.IExtendedMoveable:location.get", { params: Array<out Any> ->
                    GetLocationCmd(
                        obj = params[0] as MutableMap<String, Any>,
                    )
                })
            ),
            Ioc.Companion.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("motion.move.IExtendedMoveable:location.set", { params: Array<out Any> ->
                    SetLocationCmd(
                        obj = params[0] as MutableMap<String, Any>,
                        newValue = params[1]
                    )
                })
            ),
            Ioc.Companion.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("motion.move.IExtendedMoveable:finish", { params: Array<out Any> ->
                    FinishCmd(
                        obj = params[0] as MutableMap<String, Any>,
                    )
                })
            ),
        )

        fun init() {
            commands.forEach { it.execute() }
        }
    }
}