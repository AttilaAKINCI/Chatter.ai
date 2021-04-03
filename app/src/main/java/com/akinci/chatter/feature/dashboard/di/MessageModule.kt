package com.akinci.chatter.feature.dashboard.di

import com.akinci.chatter.common.network.NetworkChecker
import com.akinci.chatter.feature.acommon.data.local.dao.MessageDao
import com.akinci.chatter.feature.acommon.data.local.dao.UserDao
import com.akinci.chatter.feature.dashboard.data.api.MessageServiceDao
import com.akinci.chatter.feature.dashboard.repository.MessageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MessageModule {

    @Provides
    @Singleton
    fun provideMessageServiceDao(
        retrofit: Retrofit
    ): MessageServiceDao = retrofit.create(MessageServiceDao::class.java)

    @Provides
    @Singleton
    fun provideMessageRepository(
            messageServiceDao: MessageServiceDao,
            messageDao: MessageDao,
            userDao: UserDao,
            networkChecker: NetworkChecker
    ) = MessageRepository(messageServiceDao, messageDao, userDao, networkChecker)

}