package com.akinci.chatter.data.repository

import com.akinci.chatter.core.network.toResponse
import com.akinci.chatter.data.exception.UserFetchError
import com.akinci.chatter.data.exception.UserNotFound
import com.akinci.chatter.data.mapper.toData
import com.akinci.chatter.data.mapper.toDomain
import com.akinci.chatter.data.rest.user.UserServiceResponse
import com.akinci.chatter.data.room.user.UserDao
import com.akinci.chatter.domain.data.User
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import timber.log.Timber
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val httpClient: HttpClient,
    private val userDao: UserDao,
) {
    // region LOCAL
    suspend fun create(user: User) = runCatching { userDao.create(user.toData()) }

    suspend fun get(name: String) = runCatching {
        userDao.get(name) ?: throw UserNotFound()
    }.onFailure {
        Timber.e(it, "User couldn't acquired from ROOM db by name")
    }

    suspend fun get(id: Long) = runCatching {
        userDao.get(id) ?: throw UserNotFound()
    }.onFailure {
        Timber.e(it, "User couldn't acquired from ROOM db by id")
    }

    // endregion

    // region REMOTE
    suspend fun generateRandomUser() = runCatching {
        httpClient.get("api/")
            .toResponse<UserServiceResponse>()
            .map { it.results.firstOrNull() ?: throw UserFetchError() }
            .getOrThrow()
    }.map {
        it.toDomain()
    }.onFailure {
        Timber.e(it)
    }
    // endregion
}
