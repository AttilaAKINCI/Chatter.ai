package com.akinci.chatter.domain.message

import com.akinci.chatter.core.utils.DateTimeFormat
import com.akinci.chatter.data.repository.MessageRepository
import com.akinci.chatter.domain.user.UserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.time.ZonedDateTime
import javax.inject.Inject

class MessageUseCase @Inject constructor(
    private val userUseCase: UserUseCase,
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
            Timber.e(it, "Message couldn't be send. Text: $text - Owner: $ownerUserId")
        }
    }

    suspend fun getMessages(chatSessionId: Long): Flow<List<MessageItem>> {
        val loggedInUser = userUseCase.getLoggedInUser().onFailure {
            Timber.e(it, "Messaging flow can be used only when loggedInUser is available")
        }.getOrNull() ?: return emptyFlow()

        return messageRepository.getMessages(chatSessionId = chatSessionId)
            .map { messages ->
                messages.map {
                    val date = runCatching { ZonedDateTime.parse(it.date) }.getOrNull()

                    if (it.ownerUserId == loggedInUser.id) {
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
}
