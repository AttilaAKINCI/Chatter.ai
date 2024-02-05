package com.akinci.chatter.ui.features.messaging

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.chatter.core.compose.reduce
import com.akinci.chatter.core.coroutine.ContextProvider
import com.akinci.chatter.domain.ChatSimulator
import com.akinci.chatter.domain.GetPrimaryUserUseCase
import com.akinci.chatter.ui.features.messaging.MessagingViewContract.ScreenArgs
import com.akinci.chatter.ui.features.messaging.MessagingViewContract.State
import com.akinci.chatter.ui.features.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MessagingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val contextProvider: ContextProvider,
    private val getPrimaryUserUseCase: GetPrimaryUserUseCase,
    private val chatSimulator: ChatSimulator,
) : ViewModel() {
    private val screenArgs by lazy { savedStateHandle.navArgs<ScreenArgs>() }

    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(
        State(session = screenArgs.session)
    )
    val stateFlow = _stateFlow.asStateFlow()

    init {
        // get logged in user and update user related ui parts.
        getLoggedInUser()

        // activate messaging simulator.
        chatSimulator.activateOn(session = screenArgs.session)
    }

    private fun getLoggedInUser() {
        viewModelScope.launch {
            withContext(contextProvider.io) {
                getPrimaryUserUseCase.execute()
            }.onSuccess {
                // save logged in user for further needs
                _stateFlow.reduce { copy(loggedInUser = it) }

                // subscribe messages
                subscribeToMessages()
            }
        }
    }

    private fun subscribeToMessages() {
        chatSimulator.messageFlow
            .onEach { newMessages ->
                // update ui with new messages
                _stateFlow.reduce {
                    copy(messages = newMessages.toPersistentList())
                }
            }.launchIn(viewModelScope)
    }

    fun onTextChanged(typedText: String) {
        // update ui with new typed text, apply necessary validations here.
        _stateFlow.reduce { copy(text = typedText) }
    }

    fun onSendButtonClick() {
        val state = stateFlow.value
        if (state.loggedInUser == null) return

        viewModelScope.launch {
            // clear typed text on ui.
            _stateFlow.reduce { copy(text = "") }

            withContext(contextProvider.io) {
                // Send loggedInUser's message.
                chatSimulator.send(
                    chatSessionId = state.session.sessionId,
                    ownerUserId = state.loggedInUser.id,
                    text = state.text,
                )
            }
        }
    }
}
