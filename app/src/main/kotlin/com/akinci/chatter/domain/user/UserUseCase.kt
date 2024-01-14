package com.akinci.chatter.domain.user

import com.akinci.chatter.data.repository.UserRepository
import com.akinci.chatter.domain.mapper.toData
import com.akinci.chatter.domain.mapper.toDomain
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend fun verifyUser(name: String): Boolean {
        val user = userRepository.getUser(name).getOrNull()
        return user != null
    }

    suspend fun saveUser(user: User) = userRepository.createUser(user.toData())

    suspend fun getRandomUser() = userRepository.getRandomUser()
        .map { it.results.firstOrNull()?.toDomain() }
}
