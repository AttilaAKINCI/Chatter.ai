package com.akinci.chatter.data.repository

import com.akinci.chatter.data.mapper.toDomain
import com.akinci.chatter.data.room.chatsession.ChatSessionDao
import com.akinci.chatter.data.room.chatsession.ChatSessionEntity
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatSessionRepository @Inject constructor(
    private val chatSessionDao: ChatSessionDao,
) {
    fun get(memberId: Long) = chatSessionDao.get(memberId).map { sessions ->
        sessions.map { it.toDomain() }
    }

    suspend fun create(primaryUserId: Long, secondaryUserId: Long) = runCatching {
        chatSessionDao.create(
            ChatSessionEntity(primaryUserId = primaryUserId, secondaryUserId = secondaryUserId)
        )
    }
}
