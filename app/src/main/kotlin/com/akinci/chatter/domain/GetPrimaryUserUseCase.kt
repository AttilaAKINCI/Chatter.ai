package com.akinci.chatter.domain

import com.akinci.chatter.data.datastore.DataStorage
import com.akinci.chatter.data.exception.UserNotFound
import com.akinci.chatter.data.mapper.toDomain
import com.akinci.chatter.data.repository.UserRepository
import com.akinci.chatter.domain.data.User
import javax.inject.Inject

class GetPrimaryUserUseCase @Inject constructor(
    private val dataStorage: DataStorage,
    private val userRepository: UserRepository,
) {
    suspend fun execute(): Result<User> {
        val name = dataStorage.getLoggedInUsersName() ?: return Result.failure(UserNotFound())
        return userRepository.get(name).map { it.toDomain() }
    }
}
