package de.fabiexe.sjql

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runInterruptible
import kotlin.coroutines.CoroutineContext

suspend fun <T> Database.suspendTransaction(context: CoroutineContext = Dispatchers.IO, action: () -> T): T {
    return runInterruptible(context) {
        transaction(action)
    }
}