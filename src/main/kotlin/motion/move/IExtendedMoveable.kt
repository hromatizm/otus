package motion.move

import motion.Point
import motion.Vector

interface IExtendedMoveable {

    fun getVelocity(): Vector

    fun getLocation(): Point

    fun setLocation(newValue: Point)

    fun finish()
}