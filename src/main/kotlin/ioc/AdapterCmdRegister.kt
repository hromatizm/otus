package org.example.ioc

import command.ICommand
import ioc.Ioc
import motion.move.IMoveable

class AdapterCmdRegister {
    companion object {

        private val commands = listOf(
            Ioc.Companion.resolve<ICommand>(
                dependencyName = "GenerateAdapter",
                args = arrayOf(IMoveable::class.java)
            )
        )

        fun init() {
            commands.forEach { it.execute() }
        }
    }
}