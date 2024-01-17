package com.akinci.chatter.data.repository

import com.akinci.chatter.core.network.toResponse
import com.akinci.chatter.data.rest.user.UserServiceResponse
import com.akinci.chatter.data.room.AppDatabase
import com.akinci.chatter.data.room.user.UserEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val httpClient: HttpClient,
    private val database: AppDatabase,
) {
    private val userDao by lazy { database.getUserDao() }

    // region LOCAL
    suspend fun createUser(user: UserEntity) = runCatching { userDao.createUser(user) }

    suspend fun getUser(name: String) = runCatching { userDao.getUser(name) }

    suspend fun getUser(id: Long) = runCatching { userDao.getUser(id) }

    // endregion

    // region REMOTE
    suspend fun getRandomUser() = runCatching {
        httpClient.get("api/")
            .toResponse<UserServiceResponse>()
            .getOrThrow()
    }
    // endregion
}
