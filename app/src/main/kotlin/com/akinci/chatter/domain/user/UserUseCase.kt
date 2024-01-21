package com.akinci.chatter.domain.user

import com.akinci.chatter.data.datastore.DataStorage
import com.akinci.chatter.data.exception.UserNotFound
import com.akinci.chatter.data.repository.UserRepository
import com.akinci.chatter.domain.mapper.toData
import com.akinci.chatter.domain.mapper.toDomain
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val dataStorage: DataStorage,
    private val userRepository: UserRepository,
) {
    suspend fun verifyUser(name: String) = userRepository.getUser(name).getOrNull() != null

    suspend fun saveUser(user: User) = userRepository.createUser(user.toData())

    suspend fun getUser(userId: Long) = userRepository.getUser(userId).map { it.toDomain() }

    suspend fun getLoggedInUser(): Result<User> {
        val name = dataStorage.getLoggedInUsersName() ?: return Result.failure(UserNotFound())
        return userRepository.getUser(name).map { it.toDomain() }
    }

    suspend fun getRandomUser() = userRepository.getRandomUser().map { it.toDomain() }
}
