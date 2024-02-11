package com.akinci.chatter.core.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MVI<State, Action, Effect> {
    val state: StateFlow<State>
    val effect: Flow<Effect>

    fun onAction(action: Action)

    fun updateState(block: State.() -> State)

    suspend fun sendEffect(effect: Effect)
}
