package com.akinci.chatter.feature.acommon.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.akinci.chatter.common.room.RoomConfig.Companion.USER_TABLE_NAME

@Entity(tableName = USER_TABLE_NAME)
data class User constructor(
    @PrimaryKey(autoGenerate = false)
    val id:Long,
    val avatarURL:String,
    val nickname:String
)