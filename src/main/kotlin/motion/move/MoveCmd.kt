package motion.move

import exception.command.ICommand
import motion.Point
import motion.Vector
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class MoveCmd(private val obj: IMoveable) : ICommand {

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