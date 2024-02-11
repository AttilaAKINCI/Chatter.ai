package com.akinci.chatter.data.mapper

import com.akinci.chatter.data.room.chatsession.ChatSessionWithUser
import com.akinci.chatter.domain.data.ChatSession

fun ChatSessionWithUser.toDomain() = ChatSession(
    sessionId = chatSessionEntity.id,
    chatMate = userEntity.toDomain()
)