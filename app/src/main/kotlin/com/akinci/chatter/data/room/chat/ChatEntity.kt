package com.akinci.chatter.data.room.chat

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.akinci.chatter.data.room.AppDatabaseKeys

@Entity(tableName = AppDatabaseKeys.DB_TABLE_CHAT)
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val users: String,
)
