package com.akinci.chatter.domain

import com.akinci.chatter.data.repository.MessageRepository
import com.akinci.chatter.domain.data.ChatSession
import com.akinci.chatter.domain.data.MessageItem
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
 * [ChatSimulator] is a wrapper service that taps ongoing messaging flow and adds simulation of
 * chat mate's responses and adjusts typing indicator.
 *
 * [ChatSimulator] is designed to answer send messages by the actual user.
 * **/
class ChatSimulator @Inject constructor(
    private val messageRepository: MessageRepository,
    private val getMessageStreamUseCase: GetMessageStreamUseCase,
) {
    private val coroutineScope by lazy { CoroutineScope(Job() + Dispatchers.Main) }
    private var replyGeneratorJob: Job? = null
    private var chatSession: ChatSession? = null

    private val _messageFlow: MutableStateFlow<List<MessageItem>> = MutableStateFlow(listOf())
    val messageFlow = _messageFlow.asStateFlow()

    fun activateOn(session: ChatSession) {
        chatSession = session

        coroutineScope.launch {
            // subscribe to messages on chat session
            subscribeToMessagesOnChatSession(chatSessionId = session.sessionId)
        }
    }

    suspend fun send(chatSessionId: Long, ownerUserId: Long, text: String) =
        messageRepository.save(chatSessionId, ownerUserId, text)

    private suspend fun subscribeToMessagesOnChatSession(chatSessionId: Long) {
        getMessageStreamUseCase.execute(chatSessionId)
            .onEach { newMessages ->
                val outBoundCount = newMessages.count { it is MessageItem.OutboundMessageItem }
                val inBoundCount = newMessages.count { it is MessageItem.InboundMessageItem }

                if (outBoundCount > inBoundCount) {
                    // Chat mate haven't responded some of loggedInUser's messages.
                    // in order to simulate chat mate's responses, type indicator should be added.
                    simulateChatMateResponse()
                }

                _messageFlow.value =
                    if (isIndicatorActive() && isLastMessageOutBound(newMessages)) {
                        // chat mate typing in progress so we need to keep TypeIndicatorItem as 1st item
                        //  after loggedInUser's messages
                        buildList {
                            add(MessageItem.TypeIndicatorItem)
                            addAll(newMessages)
                        }
                    } else {
                        // clear typing indicator and just send messages
                        newMessages
                    }
            }.launchIn(coroutineScope)
    }

    private fun simulateChatMateResponse() {
        // skip reply request because 1 response is already in progress
        if (replyGeneratorJob?.isCompleted == false) return
        // chat session should be received to simulate response action
        if (chatSession == null) return

        replyGeneratorJob = coroutineScope.launch {
            // simulate delay before your chat mate's type start
            delay(Random.nextLong(250L, 1000L))

            // show chat mate in typing state
            if (!isIndicatorActive()) {
                _messageFlow.value = buildList {
                    addAll(messageFlow.value)
                    add(0, MessageItem.TypeIndicatorItem)
                }
            }

            // keep ui in typing state for a while
            delay(Random.nextLong(500L, 2000L))

            // send chat mate's response
            // TODO add Gemini AI for chat response texts.
            send(
                chatSessionId = chatSession!!.sessionId,
                ownerUserId = chatSession!!.chatMate.id,
                text = "Generated Random Text: ${Random.nextInt(1, 100)}",
            )
        }
    }

    private fun isLastMessageOutBound(messages: List<MessageItem>) =
        messages.firstOrNull() is MessageItem.OutboundMessageItem

    private fun isIndicatorActive() =
        messageFlow.value.firstOrNull { it is MessageItem.TypeIndicatorItem } != null
}
