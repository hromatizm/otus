package spring.cmd

import command.ICommand
import motion.Vector
import spring.registry.UniObj

class SetVelocityCmd(
    val obj: UniObj,
    val velocity: Vector,
) : ICommand {

    override fun execute() {
        obj["velocity"] = velocity
    }
}