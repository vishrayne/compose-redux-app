package com.vishrayne.myfirstreduxapp.redux

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class Store<T>(initialState: T) {
    private val _stateFlow = MutableStateFlow(initialState)
    val stateFlow: StateFlow<T> = _stateFlow

    private val mutex = Mutex()

    suspend fun update(updateBlock: (T) -> T) = mutex.withLock {
        _stateFlow.update { oldState ->
            updateBlock(oldState)
        }
    }

    suspend fun read(readBlock: (T) -> Unit) = mutex.withLock {
        readBlock(_stateFlow.value)
    }
}