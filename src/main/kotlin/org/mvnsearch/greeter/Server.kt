package org.mvnsearch.greeter

import io.grpc.ServerBuilder
import io.grpc.protobuf.services.ProtoReflectionService
import org.mvnsearch.greeter.data.database.InMemoryDB
import org.mvnsearch.greeter.data.repository.MarketRepository
import org.mvnsearch.greeter.data.repository.WalletRepository
import org.mvnsearch.greeter.service.market.MarketServiceImpl
import org.mvnsearch.greeter.service.market.WalletServiceImpl

class Server constructor(
    private val port: Int
) {
    private val inMemoryDB = InMemoryDB()

    private val marketRepository = MarketRepository(inMemoryDB)
    private val walletRepository = WalletRepository(inMemoryDB)

    private val server = ServerBuilder
        .forPort(port)
        .addService(MarketServiceImpl(marketRepository))
        .addService(WalletServiceImpl(walletRepository))
        .addService(ProtoReflectionService.newInstance())
        .build()

    fun start() {
        server.start()
        println("Server started, listening on $port")

        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** shutting down gRPC server since JVM is shutting down")
                this@Server.stop()
                println("*** server shut down")
            }
        )
    }

    private fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }
}

fun main() {
    val port = 50051
    val server = Server(port)

    server.start()
    server.blockUntilShutdown()
}
