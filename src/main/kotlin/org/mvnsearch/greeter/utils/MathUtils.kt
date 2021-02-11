package org.mvnsearch.greeter.utils

object MathUtils {
    fun randomMaxMin(max: Double, min: Double): Double {
       return (Math.random() * (max - min) + min)
    }

    fun randomMaxMin(max: Int, min: Int): Int {
        return (Math.random() * (max - min) + min).toInt()
    }
}