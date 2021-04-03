package com.akinci.chatter.feature.dashboard.data.api

import com.akinci.chatter.common.network.RestConfig
import com.akinci.chatter.feature.dashboard.data.output.MessageHistoryResponse
import retrofit2.Response
import retrofit2.http.GET

interface MessageServiceDao {
    @GET(RestConfig.MESSAGE_HISTORY_URL)
    suspend fun getUserMessageHistory() : Response<MessageHistoryResponse>
}