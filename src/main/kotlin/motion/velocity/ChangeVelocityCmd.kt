package org.example.motion.velocity

import exception.command.ICommand
import motion.Vector

class ChangeVelocityCmd(
    private val obj: IObjWithChangeableVelocity,
    private val velocity: Vector,
) : ICommand {

    override fun execute() {
        obj.setVelocity(velocity)
    }
}