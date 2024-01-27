package com.akinci.chatter.data.repository

import com.akinci.chatter.core.utils.DateTimeFormat
import com.akinci.chatter.data.room.AppDatabase
import com.akinci.chatter.data.room.message.MessageEntity
import java.time.ZonedDateTime
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val database: AppDatabase,
) {
    private val messageDao by lazy { database.getMessageDao() }

    suspend fun send(
        chatSessionId: Long,
        ownerUserId: Long,
        text: String,
        date: String,
    ) = runCatching {
        messageDao.insertMessage(
            MessageEntity(
                chatSessionId = chatSessionId,
                ownerUserId = ownerUserId,
                text = text,
                date = date,
            )
        )
    }

    fun getMessages(chatSessionId: Long) = messageDao.getHistory(chatSessionId)

}
