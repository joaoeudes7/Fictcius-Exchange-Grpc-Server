package org.mvnsearch.greeter.data.database.entities

data class Coin(
    val code: String,
    val valueBase: Double,
    var volumeSold: Long,
    val totalVolume: Long = 80000,
    var oldValue: Double = 0.0
) {
    val value: Double
        get() {
            return valueBase * (totalVolume / volumeSold)
        }

    val volumeAvailable: Long
        get() {
            return totalVolume - volumeSold
        }
}