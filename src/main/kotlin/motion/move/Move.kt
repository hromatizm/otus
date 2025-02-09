package motion.move

import motion.Point
import motion.Vector
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class Move(private val obj: IMoveable) : IMoveCmd {

    override fun execute() {
        val newLocation: Point = changeLocation(
            location = obj.getLocation(),
            vector = obj.getVelocity()
        )
        obj.setLocation(newLocation)
    }

    private fun changeLocation(location: Point, vector: Vector): Point {
        val angle: Double = Math.toRadians(vector.angle.getDegrees().toDouble())
        val deltaX: Int = (vector.abs * cos(angle)).roundToInt()
        val deltaY: Int = (vector.abs * sin(angle)).roundToInt()
        return Point(
            x = location.x + deltaX,
            y = location.y + deltaY,
        )
    }
}