package com.akinci.chatter.data.room.chatsession

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.akinci.chatter.data.room.AppDatabaseKeys

@Entity(tableName = AppDatabaseKeys.DB_TABLE_CHAT_SESSIONS)
data class ChatSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val membersIds: String,
)
