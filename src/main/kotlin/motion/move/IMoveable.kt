package motion.move

import motion.Point
import motion.Vector

interface IMoveable {

    fun getVelocity(): Vector

    fun getLocation(): Point

    fun setLocation(newLocation: Point)

}