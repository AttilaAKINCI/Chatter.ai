package com.akinci.chatter.domain.simulator

import com.akinci.chatter.domain.message.MessageItem
import com.akinci.chatter.domain.message.MessageUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

/**
 * [MessagingSimulator] is a wrapper service that taps ongoing messaging flow and adds simulation of
 * chat mate's responses and adjusts typing indicator.
 *
 * MessagingSimulator is designed to answer send messages by the actual user.
 * **/
class MessagingSimulator @Inject constructor(
    private val messageUseCase: MessageUseCase,
) {
    private val coroutineScope by lazy { CoroutineScope(Job() + Dispatchers.Main) }

    private val _messageFlow: MutableStateFlow<List<MessageItem>> = MutableStateFlow(listOf())
    val messageFlow = _messageFlow.asStateFlow()

    fun activateOn(chatSessionId: Long) {
        coroutineScope.launch {
            // subscribe to messages on chat session
            subscribeToMessagesOnChatSession(chatSessionId = chatSessionId)
        }
    }

    suspend fun send(chatSessionId: Long, ownerUserId: Long, text: String) =
        messageUseCase.send(chatSessionId, ownerUserId, text)

    private suspend fun subscribeToMessagesOnChatSession(chatSessionId: Long) {
        messageUseCase.getMessages(chatSessionId)
            .onEach { newMessages ->
                val outBoundCount = newMessages.count { it is MessageItem.OutboundMessageItem }
                val inBoundCount = newMessages.count { it is MessageItem.InboundMessageItem }

                if (outBoundCount > inBoundCount) {
                    // Chat mate haven't responded some of loggedInUser's messages.
                    // in order to simulate chat mate's responses, type indicator should be added.
                    addTypeIndicator()
                    // Request response from chat mate.
                    requestChatMateResponse()
                }

                _messageFlow.value = newMessages
            }.launchIn(coroutineScope)
    }

    private fun addTypeIndicator() {
        coroutineScope.launch {
            // simulate delay before your chat mate's type start
            delay(Random.nextLong(500L, 2000L))

            // show chat mate is typing ui state
            if (messageFlow.value.firstOrNull { it is MessageItem.TypeIndicatorItem } == null) {
                _messageFlow.value = buildList {
                    addAll(messageFlow.value)
                    add(0, MessageItem.TypeIndicatorItem)
                }
            }
        }
    }

    // TODO separate delays can break response order.
    //  fix here.
    private fun requestChatMateResponse() {

    }

    /* // if last received message is outbound we need to protect type indicator's state
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
     }*/


    /* private fun triggerSimulateChatMateResponse() {
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
     }*/
}
