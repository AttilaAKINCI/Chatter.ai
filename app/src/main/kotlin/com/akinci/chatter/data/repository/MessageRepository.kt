package com.akinci.chatter.data.repository

import com.akinci.chatter.core.utils.DateTimeFormat
import com.akinci.chatter.data.mapper.toDomain
import com.akinci.chatter.data.room.message.MessageDao
import com.akinci.chatter.data.room.message.MessageEntity
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.time.ZonedDateTime
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val messageDao: MessageDao,
) {
    fun get(chatSessionId: Long) = messageDao.get(chatSessionId)
        .map { messages -> messages.map { it.toDomain() } }

    suspend fun save(
        chatSessionId: Long,
        primaryUserId: Long,
        text: String,
    ) = runCatching {
        messageDao.save(
            MessageEntity(
                chatSessionId = chatSessionId,
                senderUserId = primaryUserId,
                text = text,
                date = DateTimeFormat.STORE.format(ZonedDateTime.now()).orEmpty(),
            )
        )
    }.onFailure {
        Timber.e(it, "Message couldn't be send. Text: $text - Owner: $primaryUserId")
    }
}
