package com.akinci.chatter.data.room.chatsession

import androidx.room.Embedded
import androidx.room.Relation
import com.akinci.chatter.data.room.user.UserEntity

data class ChatSessionWithUser(
    @Embedded
    val chatSessionEntity: ChatSessionEntity,

    @Relation(
        parentColumn = "primaryUserId",
        entityColumn = "id"
    )
    val primaryUserEntity: UserEntity,

    @Relation(
        parentColumn = "secondaryUserId",
        entityColumn = "id"
    )
    val secondaryUserEntity: UserEntity
)
