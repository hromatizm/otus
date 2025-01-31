package org.example.move

class Angle(
    val scale: Int,
    value: Int
) {

    var value: Int = 0
        set(newValue) {
            if (value < 0) throw IllegalArgumentException("Angle value cant be less than 0")
            if (value > scale) throw IllegalArgumentException("Angle value cant be greater than scale ($scale)")
            field = newValue
        }

    init {
        if (scale < 1) throw IllegalArgumentException("Angle scale cant be less than 1")
        if (value < 0) throw IllegalArgumentException("Angle value cant be less than 0")
        if (value > scale) throw IllegalArgumentException("Angle value can't be greater than scale ($scale)")
        this.value = value
    }

    val degrees: Double = value / scale * 360.0
}