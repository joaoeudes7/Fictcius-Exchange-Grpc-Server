package org.mvnsearch.greeter.utils

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.mvnsearch.greeter.data.database.entities.Coin

class MathUtilsTest {

    @Test
    fun shouldRandomNumber() = runBlocking {
        val number = MathUtils.randomMaxMin(1.0, 2.0)

        print(number)
        assert(number > 1.0 && number < 2.0)
    }


    @Test
    fun hhh() {
        val coinsFictitious = listOf(
            Coin("KLV", 0.04, 780, 1000),
            Coin("USDT", 1.0, 680, 1000)
        )

        val b = mutableListOf<Pair<Coin, Coin>>()

        coinsFictitious.forEach { coin1 ->
            coinsFictitious.filter { it != coin1 }.forEach { coin2 ->
                b.add(Pair(coin1, coin2))
            }
        }

        println(b.size)
    }
}