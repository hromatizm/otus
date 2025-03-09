package org.example

import command.ICommand
import command.IValueCommand
import ioc.Ioc
import motion.Point
import motion.move.IMoveable
import org.example.generator.CmdRegister

fun main() {
    CmdRegister.init()
    Ioc.resolve<ICommand>(
        dependencyName = "GenerateAdapter",
        args = arrayOf(IMoveable::class.java)
    ).execute()

    val obj = mutableMapOf<String, Any>()
    val instance = Ioc.resolve<IValueCommand<IMoveable>>(
        dependencyName = "Adapter",
        args = arrayOf(IMoveable::class.java, obj)
    ).execute()
    instance.setLocation(Point(10, 20))
    println("${obj["location"]}")
}
