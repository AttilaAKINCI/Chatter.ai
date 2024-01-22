package com.akinci.chatter.data.repository

import com.akinci.chatter.data.room.AppDatabase
import com.akinci.chatter.data.room.chatsession.ChatSessionEntity
import javax.inject.Inject

class ChatSessionRepository @Inject constructor(
    private val database: AppDatabase,
) {
    private val chatSessionDao by lazy { database.getChatSessionDao() }

    suspend fun createChatSession(membersIds: List<Long>) = runCatching {
        chatSessionDao.create(
            ChatSessionEntity(membersIds = ",${membersIds.joinToString(separator = ",")},")
        )
    }

    fun getChatSessions(memberId: Long) = chatSessionDao.getSessions(memberId)
}
