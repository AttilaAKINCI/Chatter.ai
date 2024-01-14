package com.akinci.chatter.data.room.message

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.akinci.chatter.data.room.AppDatabaseKeys

// TODO refactor

@Entity(
    tableName = AppDatabaseKeys.DB_TABLE_MESSAGE,
    indices = [Index(value = ["id", "dataOwnerId"], unique = true)]
)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val rowId: Long = 0L,
    val dataOwnerId: Long,
    val id: String,
    val text: String,
    val timestamp: Long,
    val messageOwnerId: Long
)