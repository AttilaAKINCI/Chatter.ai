package com.akinci.chatter.core.compose

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

interface UIState

fun <T : UIState> MutableStateFlow<T>.reduce(reducer: T.() -> T) {
    update { reducer(it) }
}