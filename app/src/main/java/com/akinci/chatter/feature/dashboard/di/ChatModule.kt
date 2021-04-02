package com.akinci.chatter.feature.dashboard.di

import com.akinci.chatter.common.network.NetworkChecker
import com.akinci.chatter.feature.dashboard.data.api.ChatServiceDao
import com.akinci.chatter.feature.dashboard.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {

    @Provides
    @Singleton
    fun provideChatServiceDao(
        retrofit: Retrofit
    ): ChatServiceDao = retrofit.create(ChatServiceDao::class.java)

    @Provides
    @Singleton
    fun provideChatRepository(
        chatServiceDao: ChatServiceDao,
        networkChecker: NetworkChecker
    ) = ChatRepository(chatServiceDao, networkChecker)

}