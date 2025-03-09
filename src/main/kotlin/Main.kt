package org.example

import command.IValueCommand
import ioc.Ioc
import motion.Point
import motion.move.IMoveable

fun main() {
    val obj = mutableMapOf<String, Any>()
    val instance = Ioc.resolve<IValueCommand<IMoveable>>(
        dependencyName = "Adapter",
        args = arrayOf(IMoveable::class.java, obj)
    ).execute()
    instance.setLocation(Point(10, 20))
    println("${obj["location"]}")
}
