package de.fabiexe.sjql

import kotlinx.coroutines.runInterruptible
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

suspend fun <T> Database.suspendingTransaction(action: () -> T, context: CoroutineContext = EmptyCoroutineContext): T {
    return runInterruptible(context) {
        transaction(action)
    }
}