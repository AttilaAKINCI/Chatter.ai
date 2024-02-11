package com.akinci.chatter.domain

import com.akinci.chatter.core.utils.DateTimeFormat
import com.akinci.chatter.data.repository.MessageRepository
import com.akinci.chatter.domain.data.MessageItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class GetMessageStreamUseCase @Inject constructor(
    private val getPrimaryUserUseCase: GetPrimaryUserUseCase,
    private val messageRepository: MessageRepository,
) {

    suspend fun execute(chatSessionId: Long): Flow<List<MessageItem>> {
        val primaryUser = getPrimaryUserUseCase.execute()
            .onFailure {
                Timber.e(it, "Messaging flow can be used only when loggedInUser is available")
            }.getOrNull() ?: return emptyFlow()

        return messageRepository.get(chatSessionId = chatSessionId)
            .map { messages ->
                messages.map {
                    if (it.sender == primaryUser) {
                        MessageItem.OutboundMessageItem(
                            text = it.text,
                            time = DateTimeFormat.CLOCK_24.format(it.date).orEmpty()
                        )
                    } else {
                        MessageItem.InboundMessageItem(
                            text = it.text,
                            time = DateTimeFormat.CLOCK_24.format(it.date).orEmpty()
                        )
                    }
                }
            }
    }
}
