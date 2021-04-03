package com.akinci.chatter.feature.acommon.di

import android.content.Context
import androidx.room.Room
import com.akinci.chatter.common.room.RoomConfig.Companion.DB_NAME
import com.akinci.chatter.feature.acommon.data.local.ChatterDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonFeatureModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            ChatterDatabase::class.java,
            DB_NAME
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideMessageDao(db: ChatterDatabase) = db.getMessageDao()

    @Provides
    @Singleton
    fun provideUserDao(db: ChatterDatabase) = db.getUserDao()

}