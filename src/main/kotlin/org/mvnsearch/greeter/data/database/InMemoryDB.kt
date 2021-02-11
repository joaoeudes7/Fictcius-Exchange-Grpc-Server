package org.mvnsearch.greeter.data.database

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.mvnsearch.greeter.data.database.entities.Coin
import org.mvnsearch.greeter.data.database.entities.Order
import org.mvnsearch.greeter.data.database.entities.PairCoin
import org.mvnsearch.greeter.data.database.entities.WalletCoin
import org.mvnsearch.greeter.utils.MathUtils.randomMaxMin
import org.mvnsearch.greeter.utils.setInterval

class InMemoryDB {
    private val walletFictitious = listOf(
        WalletCoin("KLV", 10),
        WalletCoin("USDT", 10),
    )

    private val coinsFictitious = listOf(
        Coin("KLV", 0.01, 780),
        Coin("USDT", 0.03, 680)
    )

    private val marketPairs = mutableListOf<PairCoin>()
    private val historicOrders = mutableListOf<Order>()

    init {
        setInterval(2000) {
            coinsFictitious.forEach { coin ->
                coin.oldValue = coin.value
                coin.volumeSold += randomMaxMin(-20, 60)
            }

            marketPairs.clear()

            coinsFictitious.forEach { coin1 ->
                coinsFictitious.filter { it.code != coin1.code }.map { coin2 ->
                    PairCoin(coin1.copy(), coin2.copy())
                }.let { marketPairs.addAll(it) }
            }

            println(coinsFictitious)

            scanOrders()
        }
    }

    val getMarketPrices = flow {
        while(true) {
            emit(marketPairs)
            delay(3000)
        }
    }

    fun getCoins() = flow {
        while (true) {
            emit(coinsFictitious)
            delay(3000)
        }
    }

    fun getWalletUser() = flow {
        while (true) {
            emit(walletFictitious.toList())
            delay(3000)
        }
    }

    val getHistoric = flow {
        while (true) {
            emit(historicOrders)
            delay(3000)
        }
    }

    private fun scanOrders() {
        historicOrders.filter { it.status == Order.StatusOrder.PENDING }.forEach { order ->
            resolveOrder(order)
        }
    }

    fun registerOrder(order: Order) {
        historicOrders.add(order)
    }

    private fun resolveOrder(order: Order) {
        val pair = marketPairs.find { it.code == order.pair }!!

        val coinPurchasedInMarket = pair.getCoinPurchasedFromOrder(order)
        val coinSoldInMarket = pair.getCoinSoldFromOrder(order)

        val amountCoinPurchased = (order.price / coinSoldInMarket.value) / coinPurchasedInMarket.value
        val amountCoinSold = (order.price * coinPurchasedInMarket.value) / coinSoldInMarket.value

        val coinSoldInWallet = walletFictitious.find { it.coinCode == order.coinSold }!!
        val coinPurchasedInUser = walletFictitious.find { it.coinCode == order.coinPurchased }!!

        // Add if
        val hasAmountAvailable = amountCoinSold >= order.amount

        val haveHitPrice = if (order.type == Order.TypeOrder.BUY) {
            coinPurchasedInMarket.valueBase <= order.price
        } else {
            coinPurchasedInMarket.valueBase >= order.price
        }

        if (hasAmountAvailable && haveHitPrice) {
            order.price = coinPurchasedInMarket.value

            coinSoldInWallet.apply {
                equity -= order.amount
            }

            coinPurchasedInUser.apply {
                equity += order.amount
            }

            coinPurchasedInMarket.apply {
                volumeSold -= amountCoinPurchased.toLong()
            }

            coinSoldInMarket.apply {
                volumeSold += amountCoinSold.toLong()
            }

            order.status = Order.StatusOrder.RESOLVED
        }
    }
}