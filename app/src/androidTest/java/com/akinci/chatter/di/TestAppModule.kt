package com.akinci.chatter.di

import android.content.Context
import androidx.room.Room
import com.akinci.chatter.common.storage.Preferences
import com.akinci.chatter.feature.acommon.data.local.ChatterDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class TestLocalPreference

    @Provides
    @TestLocalPreference
    fun provideLocalPreferences(
        @ApplicationContext context: Context
    ) = Preferences(context)

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class TestDB

    @Provides
    @TestDB
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(
            context, ChatterDatabase::class.java
        ).allowMainThreadQueries()
            .build()

}