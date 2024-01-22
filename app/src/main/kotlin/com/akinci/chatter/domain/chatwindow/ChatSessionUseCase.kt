package com.akinci.chatter.domain.chatwindow

import com.akinci.chatter.data.repository.ChatSessionRepository
import com.akinci.chatter.domain.user.UserUseCase
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatSessionUseCase @Inject constructor(
    private val userUseCase: UserUseCase,
    private val chatSessionRepository: ChatSessionRepository,
) {

    suspend fun createChatSession(membersIds: List<Long>) =
        chatSessionRepository.createChatSession(membersIds = membersIds)

    fun getChatSessions(memberId: Long) = chatSessionRepository.getChatSessions(memberId)
        .map {
            it.map { entity ->
                val chatMate = entity.membersIds
                    .removeSurrounding(",")
                    .split(",")
                    .filter { id -> id.toLong() != memberId }.firstNotNullOf { id ->
                        userUseCase.getUser(id.toLong()).getOrNull()
                    }

                ChatSession(sessionId = entity.id, chatMate = chatMate)
            }.reversed()
        }
}
