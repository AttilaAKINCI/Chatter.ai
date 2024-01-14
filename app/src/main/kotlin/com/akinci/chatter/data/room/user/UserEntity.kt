package com.akinci.chatter.data.room.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.akinci.chatter.data.room.AppDatabaseKeys

@Entity(tableName = AppDatabaseKeys.DB_TABLE_USER)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val userName: String,
    val imageUrl: String,
    val phone: String,
    val nationality: String,
)
