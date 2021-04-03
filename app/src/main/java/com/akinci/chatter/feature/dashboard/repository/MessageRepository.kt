package com.akinci.chatter.feature.dashboard.repository

import com.akinci.chatter.common.helper.Resource
import com.akinci.chatter.common.network.NetworkChecker
import com.akinci.chatter.common.repository.BaseRepositoryImpl
import com.akinci.chatter.feature.acommon.data.local.dao.MessageDao
import com.akinci.chatter.feature.acommon.data.local.dao.UserDao
import com.akinci.chatter.feature.acommon.data.local.entities.relations.MessageWithUser
import com.akinci.chatter.feature.dashboard.data.api.MessageServiceDao
import com.akinci.chatter.feature.dashboard.data.output.Message
import com.akinci.chatter.feature.dashboard.data.output.MessageHistoryResponse
import com.akinci.chatter.feature.dashboard.data.output.convertMessageListToMessageEntityList
import com.akinci.chatter.feature.dashboard.data.output.convertMessageListToUserEntityList
import javax.inject.Inject

class MessageRepository @Inject constructor(
        private val messageServiceDao: MessageServiceDao,
        private val messageDao: MessageDao,
        private val userDao: UserDao,
    networkChecker: NetworkChecker
) : BaseRepositoryImpl(networkChecker) {

    suspend fun getLoggedInUser(userName: String) = userDao.getLoggedInUser(userName)

    suspend fun getUserMessageHistory(): Resource<MessageHistoryResponse>
        = callService { messageServiceDao.getUserMessageHistory() }

    suspend fun insertUserHistoryMessages(dataOwnerId: Long, messageList: List<Message>) {
        messageDao.insertAllMessages(messageList.convertMessageListToMessageEntityList(dataOwnerId))
        userDao.insertAllUsers(messageList.convertMessageListToUserEntityList())
    }

    suspend fun getUserRecentMessages(dataOwnerId: Long): List<MessageWithUser>
        = messageDao.getAllMessages(dataOwnerId)

}