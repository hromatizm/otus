package org.example.move

import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class Move() : IMove {

    override fun execute(obj: IMoveable): Point {
        val newLocation: Point = changeLocation(
            location = obj.getLocation(),
            vector = obj.getVelocity()
        )
        obj.setLocation(newLocation)
        return newLocation
    }

    private fun changeLocation(location: Point, vector: Vector): Point {
        val angle: Double = Math.toRadians(vector.angle.degrees)
        val deltaX: Int = (vector.abs * cos(angle)).roundToInt()
        val deltaY: Int = (vector.abs * sin(angle)).roundToInt()
        return Point(
            x = location.x + deltaX,
            y = location.y + deltaY,
        )
    }
}

