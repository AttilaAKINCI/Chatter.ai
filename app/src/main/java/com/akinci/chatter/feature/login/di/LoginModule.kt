package com.akinci.chatter.feature.login.di

import com.akinci.chatter.feature.acommon.data.local.dao.UserDao
import com.akinci.chatter.feature.login.repository.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    @Singleton
    fun provideLoginRepository(
            userDao: UserDao
    ) = LoginRepository(userDao)

}