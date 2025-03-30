package solve

import kotlin.math.sqrt

class Solver : ISolver {

    override fun solve(a: Double, b: Double, c: Double): Array<Double> {
        checkArguments(a, b, c)
        val discriminant: Double = b * b - 4 * a * c
        return when {
            discriminant.lessThan(0.0) -> emptyArray()
            discriminant.greaterThan(0.0) -> arrayOf(
                -b + sqrt(discriminant) / (2 * a),
                -b - sqrt(discriminant) / (2 * a)
            )

            else -> Array(2) { -b / (2 * a) }
        }
    }

    private fun checkArguments(a: Double, b: Double, c: Double) {
        val args = listOf(a, b, c)
        val message = when {
            a.equalsTo(0.0) -> "first argument cannot be zero"
            args.any { it.isNaN() } -> "Argument cannot be NaN"
            args.any { it == Double.POSITIVE_INFINITY } -> "Argument cannot be positive infinite"
            args.any { it == Double.NEGATIVE_INFINITY } -> "Argument cannot be negative infinite"
            else -> null
        }
        message?.let { throw IllegalArgumentException(it) }
    }
}