package com.akinci.chatter.ui.features.messaging

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.chatter.core.coroutine.ContextProvider
import com.akinci.chatter.core.mvi.MVI
import com.akinci.chatter.core.mvi.mvi
import com.akinci.chatter.domain.ChatSimulator
import com.akinci.chatter.domain.GetPrimaryUserUseCase
import com.akinci.chatter.ui.features.messaging.MessagingViewContract.Action
import com.akinci.chatter.ui.features.messaging.MessagingViewContract.Effect
import com.akinci.chatter.ui.features.messaging.MessagingViewContract.ScreenArgs
import com.akinci.chatter.ui.features.messaging.MessagingViewContract.State
import com.akinci.chatter.ui.features.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
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
) : ViewModel(), MVI<State, Action, Effect> by mvi(State()) {

    override fun onAction(action: Action) {
        when (action) {
            Action.OnSendButtonClick -> onSendButtonClick()
            is Action.OnTextChange -> onTextChanged(action.text)
        }
    }

    private val screenArgs by lazy { savedStateHandle.navArgs<ScreenArgs>() }

    init {
        // set screen args to state
        updateState { copy(session = screenArgs.session) }

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
                updateState { copy(loggedInUser = it) }

                // subscribe messages
                subscribeToMessages()
            }
        }
    }

    private fun subscribeToMessages() {
        chatSimulator.messageFlow
            .onEach { newMessages ->
                // update ui with new messages
                updateState { copy(messages = newMessages.toPersistentList()) }
            }.launchIn(viewModelScope)
    }

    fun onTextChanged(typedText: String) {
        // update ui with new typed text, apply necessary validations here.
        updateState { copy(text = typedText) }
    }

    fun onSendButtonClick() {
        val state = state.value
        if (state.loggedInUser == null) return
        if (state.session == null) return
        if (state.text.isBlank()) return

        viewModelScope.launch {
            // clear typed text on ui.
            updateState { copy(text = "") }

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
