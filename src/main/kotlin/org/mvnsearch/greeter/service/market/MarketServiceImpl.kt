package org.mvnsearch.greeter.service.market

import klever.challenge.grpc.*
import kotlinx.coroutines.flow.*
import org.mvnsearch.greeter.data.database.entities.Order
import org.mvnsearch.greeter.data.repository.MarketRepository

class MarketServiceImpl(
    private val marketRepository: MarketRepository
) : MarketServiceGrpcKt.MarketServiceCoroutineImplBase() {
    override fun getPairsMarketStream(request: GetPairsMarketRequest) = flow {
        marketRepository.getPrices().collect {
            val response = GetPairsMarketResponse.newBuilder().apply {
                it.forEach { pair ->
                    addPairsBuilder().apply {
                        code = pair.code
                        price = pair.price
                        percentDiff = pair.percentDiff
                        volumeAvailable = pair.volumeAvailable
                    }.build()
                }
            }.build()

            emit(response)
        }
    }

    override fun getCoinsStream(request: GetCoinRequest) = flow {
        marketRepository.getCoins().collect {
            val response = GetCoinResponse.newBuilder().apply {
                it.forEach { coin ->
                    addCoinsBuilder().apply {
                        code = coin.code
                        available = coin.volumeAvailable
                        value = coin.value
                    }.build()
                }
            }.build()

            emit(response)
        }
    }

    override fun getOrders(request: GetOrderRequest) = flow {
        marketRepository.getOrders().collect {
            val response = GetOrderResponse.newBuilder().apply {
                it.forEach { order ->
                    addOrdersBuilder().apply {
                        id = order.id.toString()
                        amount = order.amount
                        pair = order.pair
                        type = TypeOrder.valueOf(order.type.name)
                        price = order.price
                        status = StatusOrder.valueOf(order.status.name)
                    }.build()
                }
            }.build()

            emit(response)
        }

    }

    override suspend fun registerOrder(request: RegisterOrderRequest): RegisterOrderResponse {
        val order = Order.fromOrderRequest(request)

        marketRepository.registerOrder(order)

        return RegisterOrderResponse.newBuilder().build()
    }
}