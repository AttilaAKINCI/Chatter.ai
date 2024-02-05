package com.akinci.chatter.data.room.message

import androidx.room.Embedded
import androidx.room.Relation
import com.akinci.chatter.data.room.user.UserEntity

data class MessageWithUser(
    @Embedded
    val messageEntity: MessageEntity,

    @Relation(
        parentColumn = "senderUserId",
        entityColumn = "id"
    )
    val userEntity: UserEntity
)
