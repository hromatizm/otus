package org.example.move

import kotlin.math.cos
import kotlin.math.sin

class Move() : IMove {

    override fun execute(obj: IMoveable) {
        val newLocation: Point = changeLocation(
            location = obj.getLocation(),
            vector = obj.getVelocity()
        )
        obj.setLocation(newLocation)
    }

    private fun changeLocation(location: Point, vector: Vector): Point {
        val angle: Double = vector.angle.degrees
        val deltaX: Int = vector.abs * cos(angle).toInt()
        val deltaY: Int = vector.abs * sin(angle).toInt()
        return Point(
            x = location.x + deltaX,
            y = location.y + deltaY,
        )
    }
}

