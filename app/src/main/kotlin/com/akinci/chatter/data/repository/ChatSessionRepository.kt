package com.akinci.chatter.data.repository

import com.akinci.chatter.data.mapper.toDomain
import com.akinci.chatter.data.room.chatsession.ChatSessionDao
import com.akinci.chatter.data.room.chatsession.ChatSessionEntity
import com.akinci.chatter.domain.data.ChatSession
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatSessionRepository @Inject constructor(
    private val chatSessionDao: ChatSessionDao,
) {
    fun getChatSessionStream(memberId: Long) = chatSessionDao.getStream(memberId).map { sessions ->
        sessions.map {
            ChatSession(
                sessionId = it.chatSessionEntity.id,
                chatMate = if (it.primaryUserEntity.id == memberId) {
                    it.secondaryUserEntity
                } else {
                    it.primaryUserEntity
                }.toDomain()
            )
        }
    }

    suspend fun create(primaryUserId: Long, secondaryUserId: Long) = runCatching {
        chatSessionDao.create(
            ChatSessionEntity(primaryUserId = primaryUserId, secondaryUserId = secondaryUserId)
        )
    }
}
