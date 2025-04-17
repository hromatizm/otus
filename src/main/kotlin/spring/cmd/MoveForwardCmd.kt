package spring.cmd

import command.ICommand
import motion.Point
import motion.Vector
import spring.registry.UniObj
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class MoveForwardCmd(
    val obj: UniObj,
) : ICommand {

    override fun execute() {
        val newLocation: Point = changeLocation(
            location = obj["location"] as Point,
            vector = obj["velocity"] as Vector
        )
        obj["location"] = newLocation
    }

    private fun changeLocation(location: Point, vector: Vector): Point {
        val angle: Double = Math.toRadians(vector.angle.getDegrees())
        val deltaX: Int = (vector.abs * cos(angle)).roundToInt()
        val deltaY: Int = (vector.abs * sin(angle)).roundToInt()
        return Point(
            x = location.x + deltaX,
            y = location.y + deltaY,
        )
    }
}