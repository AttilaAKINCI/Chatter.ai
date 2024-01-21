package com.akinci.chatter.data.repository

import com.akinci.chatter.data.room.AppDatabase
import com.akinci.chatter.data.room.chat.ChatSessionEntity
import javax.inject.Inject

class ChatSessionRepository @Inject constructor(
    private val database: AppDatabase,
) {
    private val chatSessionDao by lazy { database.getChatSessionDao() }

    suspend fun createChatSession(membersIds: List<Long>) = runCatching {
        chatSessionDao.create(
            ChatSessionEntity(membersIds = membersIds.joinToString(separator = ","))
        )
    }

    suspend fun getChatSessions(memberId: Long) = runCatching {
        chatSessionDao.getSessions(memberId)
    }
}
