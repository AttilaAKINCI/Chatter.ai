package com.akinci.chatter.data.room.chatsession

import androidx.room.Embedded
import androidx.room.Relation
import com.akinci.chatter.data.room.user.UserEntity

data class ChatSessionWithUser(
    @Embedded
    val chatSessionEntity: ChatSessionEntity,

    @Relation(
        parentColumn = "secondaryUserId",
        entityColumn = "id"
    )
    val userEntity: UserEntity
)
