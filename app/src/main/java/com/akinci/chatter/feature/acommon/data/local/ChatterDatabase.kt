package com.akinci.chatter.feature.acommon.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.akinci.chatter.feature.acommon.data.local.dao.MessageDao
import com.akinci.chatter.feature.acommon.data.local.entities.Message
import com.akinci.chatter.feature.acommon.data.local.entities.User

@Database( entities = [User::class, Message::class], version = 1, exportSchema = false)
abstract class  ChatterDatabase : RoomDatabase() {
    abstract fun getChatDao() : MessageDao
}