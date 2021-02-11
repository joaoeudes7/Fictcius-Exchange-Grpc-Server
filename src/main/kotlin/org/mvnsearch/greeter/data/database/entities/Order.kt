package org.mvnsearch.greeter.data.database.entities

import klever.challenge.grpc.RegisterOrderRequest
import java.util.*

data class Order(
    val id: UUID = UUID.randomUUID(),
    val type: TypeOrder,
    val pair: String,
    val amount: Long,
    var price: Double,
    var status: StatusOrder
) {
    companion object {
        fun fromOrderRequest(request: RegisterOrderRequest): Order {
            return Order(
                pair = request.pair,
                amount = request.amount,
                price = request.price,
                type = TypeOrder.values()[request.typeValue],
                status = StatusOrder.PENDING,
            )
        }

        private val fee = 0.01
    }

    enum class TypeOrder() {
        BUY,
        SELL
    }

    enum class StatusOrder() {
        PENDING,
        RESOLVED
    }

    val coins: List<String>
        get() = pair.split("/")

    val coinPurchased: String
        get() = if (type == TypeOrder.BUY) {
            coins.first()
        } else {
            coins.last()
        }


    val coinSold: String
        get() = if (type == TypeOrder.BUY) {
            coins.last()
        } else {
            coins.first()
        }

    val feeCost: Double
        get() {
            return (amount * price) * fee
        }
}