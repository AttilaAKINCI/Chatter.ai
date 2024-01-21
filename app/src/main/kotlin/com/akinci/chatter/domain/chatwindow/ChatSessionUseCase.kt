package com.akinci.chatter.domain.chatwindow

import com.akinci.chatter.data.repository.ChatSessionRepository
import com.akinci.chatter.domain.user.UserUseCase
import kotlinx.coroutines.delay
import javax.inject.Inject

class ChatSessionUseCase @Inject constructor(
    private val userUseCase: UserUseCase,
    private val chatSessionRepository: ChatSessionRepository,
) {

    suspend fun createChatSession(membersIds: List<Long>) =
        chatSessionRepository.createChatSession(membersIds = membersIds)

    suspend fun getChatSessions(): Result<List<ChatSession>> = runCatching {
        // Simulate network delay
        delay(500L)

        val sender = userUseCase.getLoggedInUser().getOrThrow()
        val chatSessions = chatSessionRepository.getChatSessions(sender.id).getOrThrow()

        chatSessions.map {
            val members = it.membersIds.split(",").mapNotNull { id ->
                userUseCase.getUser(id.toLong()).getOrNull()
            }

            ChatSession(sessionId = it.id, members = members)
        }
    }
}
