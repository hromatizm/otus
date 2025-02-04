package org.example.motion.rotate

import motion.Angle
import motion.rotate.IRotatable
import motion.rotate.IRotation

class Rotation() : IRotation {

    override fun execute(obj: IRotatable, degreesDelta: Double) {
        val newDegrees = obj.getAngle().getDegrees() + degreesDelta
        val newAngle = Angle(newDegrees)
        obj.setAngle(newAngle)
    }
}