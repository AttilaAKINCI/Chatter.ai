package com.akinci.chatter.domain.message

import com.akinci.chatter.core.utils.DateTimeFormat
import com.akinci.chatter.data.repository.MessageRepository
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.time.ZonedDateTime
import javax.inject.Inject

class MessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository,
) {
    suspend fun send(chatSessionId: Long, ownerUserId: Long, text: String) {
        // acquire current time to add as action time
        val currentTime = DateTimeFormat.STORE.format(ZonedDateTime.now()).orEmpty()

        // save send message into local storage
        messageRepository.send(
            chatSessionId = chatSessionId,
            ownerUserId = ownerUserId,
            text = text,
            date = currentTime,
        ).onFailure {
            Timber.e(it, "Message couldn't be send. $text")
        }
    }

    fun getMessages(
        chatSessionId: Long,
        loggedInUserId: Long,
    ) = messageRepository.getMessages(chatSessionId = chatSessionId)
        .map { messages ->
            messages.map {
                val date = runCatching { ZonedDateTime.parse(it.date) }.getOrNull()

                if (it.ownerUserId == loggedInUserId) {
                    MessageItem.OutboundMessageItem(
                        text = it.text,
                        time = DateTimeFormat.CLOCK_24.format(date).orEmpty()
                    )
                } else {
                    MessageItem.InboundMessageItem(
                        text = it.text,
                        time = DateTimeFormat.CLOCK_24.format(date).orEmpty()
                    )
                }
            }
        }
}
