package com.akinci.chatter.domain.user

import com.akinci.chatter.core.coroutine.ContextProvider
import com.akinci.chatter.data.repository.UserRepository
import com.akinci.chatter.domain.mapper.toData
import com.akinci.chatter.domain.mapper.toDomain
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val contextProvider: ContextProvider,
    private val userRepository: UserRepository,
) {
    suspend fun verifyUser(name: String) = withContext(contextProvider.io) {
        userRepository.getUser(name).getOrNull()
    } != null

    suspend fun saveUser(user: User) = withContext(contextProvider.io) {
        userRepository.createUser(user.toData())
    }

    suspend fun getRandomUser() = withContext(contextProvider.io) {
        userRepository.getRandomUser().map {
            it.results.firstOrNull()?.toDomain()
        }
    }
}
