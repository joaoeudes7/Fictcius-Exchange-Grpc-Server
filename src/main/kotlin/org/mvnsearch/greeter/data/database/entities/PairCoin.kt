package org.mvnsearch.greeter.data.database.entities

class PairCoin(
    val coin1: Coin,
    val coin2: Coin,
) {
    val code: String
        get() {
            return "${coin1.code}/${coin2.code}"
        }

    // Coins AAA/BBB (10/0.1) = 100 of BBB
    val price: Double
        get() {
            return coin1.value / coin2.value
        }

    val oldPrice: Double
        get() {
            return coin1.oldValue / coin2.oldValue
        }

    val percentDiff: Double
        get() {
            return oldPrice / price
        }

    val volumeAvailable: Long
        get() {
            return coin1.volumeAvailable
        }

    fun getCoinPurchasedFromOrder(order: Order): Coin {
        return if (order.type == Order.TypeOrder.BUY) {
            coin1
        } else {
            coin2
        }
    }

    fun getCoinSoldFromOrder(order: Order): Coin {
        return if (order.type == Order.TypeOrder.BUY) {
            coin2
        } else {
            coin1
        }
    }

    override fun toString(): String {
        return "[$code - $price]"
    }
}