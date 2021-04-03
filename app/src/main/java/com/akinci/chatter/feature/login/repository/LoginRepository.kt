package com.akinci.chatter.feature.login.repository

import com.akinci.chatter.feature.acommon.data.local.dao.UserDao
import com.akinci.chatter.feature.acommon.data.local.entities.UserEntity
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun insertUser(userEntity: UserEntity) = userDao.insertUser(userEntity)
    suspend fun getLoggedInUser(userName: String) = userDao.getLoggedInUser(userName)
}