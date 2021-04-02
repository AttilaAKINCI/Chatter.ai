package com.akinci.chatter.feature.dashboard.repository

import com.akinci.chatter.common.helper.Resource
import com.akinci.chatter.common.network.NetworkChecker
import com.akinci.chatter.common.repository.BaseRepositoryImpl
import com.akinci.chatter.feature.dashboard.data.api.ChatServiceDao
import com.akinci.chatter.feature.dashboard.data.output.ChatHistoryResponse
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val chatServiceDao: ChatServiceDao,
    networkChecker: NetworkChecker
) : BaseRepositoryImpl(networkChecker) {

    suspend fun getUserChatHistory() : Resource<ChatHistoryResponse> {
        return callService { chatServiceDao.getUserChatHistory() }
    }

}