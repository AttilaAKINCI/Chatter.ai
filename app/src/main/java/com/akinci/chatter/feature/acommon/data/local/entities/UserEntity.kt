package com.akinci.chatter.feature.acommon.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.akinci.chatter.common.room.RoomConfig
import com.akinci.chatter.common.room.RoomConfig.Companion.USER_TABLE_NAME

@Entity(tableName = USER_TABLE_NAME, indices = [Index(value = ["id"], unique = true)])
data class UserEntity constructor(
    @PrimaryKey(autoGenerate = false)
    val id:Long,
    val loggedIdUser:Boolean,
    val avatarURL:String,
    val nickname:String
)