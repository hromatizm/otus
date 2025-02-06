package org.example.motion.rotate

import motion.Angle
import motion.rotate.IRotatable
import motion.rotate.IRotation

class Rotation(
    private val obj: IRotatable,
    private val degreesDelta: Double,
) : IRotation {

    override fun execute() {
        val newDegrees = obj.getAngle().getDegrees() + degreesDelta
        val newAngle = Angle(newDegrees)
        obj.setAngle(newAngle)
    }
}