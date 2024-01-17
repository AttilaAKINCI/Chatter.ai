package com.akinci.chatter.domain.user

import com.akinci.chatter.data.repository.MessageRepository
import com.akinci.chatter.data.repository.UserRepository
import com.akinci.chatter.domain.exception.LoggedInUserNotFound
import com.akinci.chatter.domain.mapper.toData
import com.akinci.chatter.domain.mapper.toDomain
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository,
) {
    suspend fun verifyUser(name: String) = userRepository.getUser(name).getOrNull() != null

    suspend fun saveUser(user: User) = userRepository.createUser(user.toData())

    suspend fun getRandomUser() = userRepository.getRandomUser().map {
        it.results.firstOrNull()?.toDomain()
    }

    suspend fun getChatMembers(loggedInUserName: String): Result<List<User>> {
        // Simulate network delay
        delay(500L)

        val sender = userRepository.getUser(loggedInUserName)
            .onFailure { Timber.e(it) }
            .getOrNull() ?: return Result.failure(LoggedInUserNotFound())

        return messageRepository.getReceiverIds(sender.id).map { ids ->
            ids.mapNotNull { id -> userRepository.getUser(id).getOrNull()?.toDomain() }
        }
    }
}
