package motion.move

import motion.IUniversalObject
import motion.Point
import motion.Vector

class MovableAdapter(
    val obj: IUniversalObject,
) : IMoveable {

    override fun getVelocity(): Vector {
        return obj.getProperty("velocity") as Vector
    }

    override fun getLocation(): Point {
        return obj.getProperty("location") as Point
    }

    override fun setLocation(newLocation: Point) {
        obj.setProperty("location" to newLocation)
    }
}