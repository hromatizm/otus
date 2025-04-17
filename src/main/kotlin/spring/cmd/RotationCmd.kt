package spring.cmd

import command.ICommand
import motion.Angle
import motion.Vector
import spring.registry.UniObj

class RotationCmd(
    val obj: UniObj,
    val args: MutableMap<String, Any>,
) : ICommand {

    override fun execute() {
        val velocity = obj["velocity"] as Vector
        val degreesDelta = args["degreesDelta"] as Double
        val newDegrees = velocity.angle.getDegrees() + degreesDelta
        val newAngle = Angle(newDegrees)
        obj["velocity"] = velocity.copy(angle = newAngle)
    }
}