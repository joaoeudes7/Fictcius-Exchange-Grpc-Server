package org.mvnsearch.greeter.data.repository

import kotlinx.coroutines.flow.Flow
import org.mvnsearch.greeter.data.database.InMemoryDB
import org.mvnsearch.greeter.data.database.entities.Coin
import org.mvnsearch.greeter.data.database.entities.Order
import org.mvnsearch.greeter.data.database.entities.PairCoin
import org.mvnsearch.greeter.data.database.entities.WalletCoin

class MarketRepository(
    private val inMemoryDB: InMemoryDB
) {
    fun getPrices(): Flow<List<PairCoin>> {
        return inMemoryDB.getMarketPrices
    }

    fun registerOrder(order: Order) {
        inMemoryDB.registerOrder(order)
    }

    fun getOrders(): Flow<MutableList<Order>> {
        return inMemoryDB.getHistoric
    }

    fun getCoins(): Flow<List<Coin>> {
        return inMemoryDB.getCoins()
    }
}