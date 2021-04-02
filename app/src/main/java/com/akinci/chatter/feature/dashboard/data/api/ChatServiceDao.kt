package com.akinci.chatter.feature.dashboard.data.api

import com.akinci.chatter.common.network.RestConfig
import com.akinci.chatter.feature.dashboard.data.output.ChatHistoryResponse
import retrofit2.Response
import retrofit2.http.GET

interface ChatServiceDao {
    @GET(RestConfig.CHAT_HISTORY_URL)
    suspend fun getUserChatHistory() : Response<ChatHistoryResponse>
}