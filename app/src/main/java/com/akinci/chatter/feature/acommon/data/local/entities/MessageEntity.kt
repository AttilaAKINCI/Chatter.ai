package com.akinci.chatter.feature.acommon.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.akinci.chatter.common.room.RoomConfig.Companion.MESSAGE_TABLE_NAME

@Entity(tableName = MESSAGE_TABLE_NAME, indices = [Index(value = ["id", "dataOwnerId"], unique = true)])
data class MessageEntity constructor(
    @PrimaryKey(autoGenerate = true)
    val rowId:Long = 0L,
    val dataOwnerId:Long,
    val id:String,
    val text:String,
    val timestamp:Long,
    val messageOwnerId:Long
)