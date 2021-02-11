package org.mvnsearch.greeter.service.market

import klever.challenge.grpc.GetWalletRequest
import klever.challenge.grpc.GetWalletResponse
import klever.challenge.grpc.WalletServiceGrpcKt
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.mvnsearch.greeter.data.repository.WalletRepository

class WalletServiceImpl(
    private val walletRepository: WalletRepository
) : WalletServiceGrpcKt.WalletServiceCoroutineImplBase() {

    override fun getWalletStream(request: GetWalletRequest) = flow {
        walletRepository.getWalletUser().collect {
            val response = GetWalletResponse.newBuilder().apply {
                it.forEach { walletCoin ->
                    addCoinsBuilder().apply {
                        coinCode = walletCoin.coinCode
                        equity = walletCoin.equity
                    }.build()
                }
            }.build()

            emit(response)
        }
    }

}