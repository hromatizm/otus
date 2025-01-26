package org.example

import kotlin.math.abs

const val EPSILON = 1e-10

fun Double.equalsTo(other: Double): Boolean {
    return abs(this - other) <= EPSILON
}

fun Double.lessThan(other: Double): Boolean {
    return (other - this) > EPSILON
}

fun Double.greaterThan(other: Double): Boolean {
    return (this - other) > EPSILON
}