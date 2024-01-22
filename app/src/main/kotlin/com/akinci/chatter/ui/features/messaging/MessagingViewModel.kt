package com.akinci.chatter.ui.features.messaging

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.akinci.chatter.ui.features.messaging.MessagingViewContract.ScreenArgs
import com.akinci.chatter.ui.features.messaging.MessagingViewContract.State
import com.akinci.chatter.ui.features.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MessagingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val screenArgs by lazy { savedStateHandle.navArgs<ScreenArgs>() }

    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(
        State(session = screenArgs.session)
    )
    val stateFlow = _stateFlow.asStateFlow()

}
