package org.mvnsearch.greeter.data.repository

import kotlinx.coroutines.flow.Flow
import org.mvnsearch.greeter.data.database.InMemoryDB
import org.mvnsearch.greeter.data.database.entities.WalletCoin

class WalletRepository(
    private val inMemoryDB: InMemoryDB
) {
    fun getWalletUser(): Flow<List<WalletCoin>> {
        return inMemoryDB.getWalletUser()
    }
}