package org.mvnsearch.greeter.utils

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun setInterval(timeMillis: Long, handler: () -> Unit) = GlobalScope.launch {
    while (true) {
        handler()
        delay(timeMillis)
    }
}