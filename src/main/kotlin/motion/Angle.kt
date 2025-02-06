package motion

private const val FULL_CIRCLE = 360.0

data class Angle(private val degrees: Double = 0.0) {

    private var angleDegrees: Double = adoptDegreesTo360(degrees)

    fun getDegrees() = angleDegrees

    fun setDegrees(degrees: Double) {
        angleDegrees = adoptDegreesTo360(degrees)
    }

    private fun adoptDegreesTo360(degrees: Double): Double {
        return ((degrees % FULL_CIRCLE) + FULL_CIRCLE) % FULL_CIRCLE
    }
}
