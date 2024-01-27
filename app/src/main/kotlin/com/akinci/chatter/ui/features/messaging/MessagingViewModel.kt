package com.akinci.chatter.ui.features.messaging

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.chatter.core.compose.reduce
import com.akinci.chatter.core.coroutine.ContextProvider
import com.akinci.chatter.domain.message.MessageItem
import com.akinci.chatter.domain.message.MessageUseCase
import com.akinci.chatter.domain.user.UserUseCase
import com.akinci.chatter.ui.features.messaging.MessagingViewContract.ScreenArgs
import com.akinci.chatter.ui.features.messaging.MessagingViewContract.State
import com.akinci.chatter.ui.features.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MessagingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val contextProvider: ContextProvider,
    private val userUseCase: UserUseCase,
    private val messageUseCase: MessageUseCase,
) : ViewModel() {
    private val screenArgs by lazy { savedStateHandle.navArgs<ScreenArgs>() }

    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(
        State(session = screenArgs.session)
    )
    val stateFlow = _stateFlow.asStateFlow()

    init {
        getLoggedInUser()
    }

    private fun getLoggedInUser() {
        viewModelScope.launch {
            withContext(contextProvider.io) {
                userUseCase.getLoggedInUser()
            }.onSuccess {
                // save logged in user for further needs
                _stateFlow.reduce { copy(loggedInUser = it) }
                // subscribe to messages on chat session
                subscribeToMessagesOnChatSession(
                    chatSessionId = screenArgs.session.sessionId,
                    loggedInUserId = it.id,
                )
            }
        }
    }

    private fun subscribeToMessagesOnChatSession(chatSessionId: Long, loggedInUserId: Long) {
        messageUseCase.getMessages(chatSessionId = chatSessionId, loggedInUserId = loggedInUserId)
            .onEach { newMessages ->
                // if last received message is outbound we need to protect type indicator's state
                //  otherwise we need remove type indicator
                val indicator = newMessages.firstOrNull {
                    it is MessageItem.OutboundMessageItem
                }?.let {
                    // if there is typing indicator on ui, protect it's state
                    stateFlow.value.messages.firstOrNull { it is MessageItem.TypeIndicatorItem }
                }

                _stateFlow.reduce {
                    copy(
                        messages = buildList {
                            addAll(newMessages)
                            indicator?.let { add(0, it) }
                        }.toPersistentList()
                    )
                }
            }.launchIn(viewModelScope)
    }

    fun onTextChanged(typedText: String) {
        _stateFlow.reduce { copy(text = typedText) }
    }

    fun onSendButtonClick() {
        val state = stateFlow.value
        if (state.loggedInUser == null) return

        viewModelScope.launch {
            // clear typed text
            _stateFlow.reduce { copy(text = "") }

            withContext(contextProvider.io) {
                // send your message
                messageUseCase.send(
                    chatSessionId = state.session.sessionId,
                    ownerUserId = state.loggedInUser.id,
                    text = state.text,
                )
            }
        }

        // simulate your chat mate's response.
        triggerSimulateChatMateResponse()
    }

    private fun triggerSimulateChatMateResponse() {
        viewModelScope.launch {
            // simulate delay before your chat mate's type start
            delay(Random.nextLong(500L, 2000L))

            // show chat mate is typing ui state
            if (stateFlow.value.messages.firstOrNull { it is MessageItem.TypeIndicatorItem } == null) {
                _stateFlow.reduce {
                    copy(messages = messages.add(0, MessageItem.TypeIndicatorItem))
                }
            }

            // Generate & Save your chat mate's message, changes will be automatically applied on messaging board.
            delay(Random.nextLong(500L, 2000L))
            val state = stateFlow.value
            withContext(contextProvider.io) {
                // send your message
                messageUseCase.send(
                    chatSessionId = state.session.sessionId,
                    ownerUserId = state.session.chatMate.id,
                    text = "Chat mate's response" // TODO provide Gemini response here.
                )
            }
        }
    }
}
