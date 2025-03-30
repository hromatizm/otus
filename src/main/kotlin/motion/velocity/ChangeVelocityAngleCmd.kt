package motion.velocity

import command.ICommand
import motion.Angle
import motion.Vector

class ChangeVelocityAngleCmd(
    val obj: IObjWithChangeableVelocity,
    val angle: Angle,
) : ICommand {

    override fun execute() {
        val currentVelocity = obj.getVelocity()
        val currentAbs = currentVelocity.abs
        val currentAngle = currentVelocity.angle
        val newDegrees = currentAngle.getDegrees() + angle.getDegrees()
        val newAngle = Angle(newDegrees)
        val newVelocity = Vector(currentAbs, newAngle)
        obj.setVelocity(newVelocity)
    }
}