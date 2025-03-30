package motion.rotate

import command.ICommand
import motion.Angle
import motion.rotate.IRotatable

class RotationCmd(
    val obj: IRotatable,
    val degreesDelta: Double,
) : ICommand {

    override fun execute() {
        val newDegrees = obj.getAngle().getDegrees() + degreesDelta
        val newAngle = Angle(newDegrees)
        obj.setAngle(newAngle)
    }
}