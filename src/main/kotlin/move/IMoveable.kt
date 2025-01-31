package org.example.move

interface IMoveable {



    fun getVelocity(): Vector

    fun getLocation(): Point

    fun setLocation(newLocation: Point)

}