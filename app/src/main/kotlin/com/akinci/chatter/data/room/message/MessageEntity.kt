package com.akinci.chatter.data.room.message

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.akinci.chatter.data.room.AppDatabaseKeys

@Entity(tableName = AppDatabaseKeys.DB_TABLE_MESSAGE)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val chatWindowId: Long,
    val senderUserId: Long,
    val time: String,
    val text: String,
)
