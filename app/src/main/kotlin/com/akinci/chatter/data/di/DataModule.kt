package com.akinci.chatter.data.di

import android.content.Context
import androidx.room.Room
import com.akinci.chatter.data.room.AppDatabase
import com.akinci.chatter.data.room.AppDatabaseKeys
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabaseKeys.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
}