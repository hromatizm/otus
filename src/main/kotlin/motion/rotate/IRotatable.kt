package motion.rotate

import motion.Angle

interface IRotatable {

    fun getAngle(): Angle

    fun setAngle(newAngle: Angle)
}