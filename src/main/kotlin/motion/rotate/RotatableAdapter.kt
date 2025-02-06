package motion.rotate

import motion.Angle
import motion.IUniversalObject

class RotatableAdapter(
    val obj: IUniversalObject,
) : IRotatable {

    override fun getAngle(): Angle {
        return obj.getProperty("angle") as Angle
    }

    override fun setAngle(newAngle: Angle) {
        obj.setProperty("angle" to newAngle)
    }
}