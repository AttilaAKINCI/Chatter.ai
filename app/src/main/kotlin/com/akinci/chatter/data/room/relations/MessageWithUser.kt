package com.akinci.chatter.data.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.akinci.chatter.data.room.message.MessageEntity
import com.akinci.chatter.data.room.user.UserEntity

// TODO refactor

data class MessageWithUser(
    @Embedded
    val messageEntity: MessageEntity,

    @Relation(
        parentColumn = "messageOwnerId",
        entityColumn = "id"
    )
    val userEntity: UserEntity
)
