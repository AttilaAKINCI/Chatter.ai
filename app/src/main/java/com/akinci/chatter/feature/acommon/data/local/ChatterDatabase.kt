package com.akinci.chatter.feature.acommon.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.akinci.chatter.feature.acommon.data.local.dao.MessageDao
import com.akinci.chatter.feature.acommon.data.local.dao.UserDao
import com.akinci.chatter.feature.acommon.data.local.entities.MessageEntity
import com.akinci.chatter.feature.acommon.data.local.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        MessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class  ChatterDatabase : RoomDatabase() {
    abstract fun getMessageDao() : MessageDao
    abstract fun getUserDao() : UserDao
}