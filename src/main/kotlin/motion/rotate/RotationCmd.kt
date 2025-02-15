package org.example.motion.rotate

import exception.command.ICommand
import motion.Angle
import motion.rotate.IRotatable

class RotationCmd(
    private val obj: IRotatable,
    private val degreesDelta: Double,
) : ICommand {

    override fun execute() {
        val newDegrees = obj.getAngle().getDegrees() + degreesDelta
        val newAngle = Angle(newDegrees)
        obj.setAngle(newAngle)
    }
}