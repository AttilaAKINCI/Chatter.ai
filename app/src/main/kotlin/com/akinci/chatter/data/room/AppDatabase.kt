package com.akinci.chatter.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.akinci.chatter.data.room.message.MessageDao
import com.akinci.chatter.data.room.message.MessageEntity
import com.akinci.chatter.data.room.user.UserDao
import com.akinci.chatter.data.room.user.UserEntity

@Database(
    entities = [MessageEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMessageDao(): MessageDao
    abstract fun getUserDao(): UserDao
}
