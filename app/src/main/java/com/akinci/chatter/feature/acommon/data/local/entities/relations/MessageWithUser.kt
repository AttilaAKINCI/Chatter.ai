package com.akinci.chatter.feature.acommon.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.akinci.chatter.feature.acommon.data.local.entities.Message
import com.akinci.chatter.feature.acommon.data.local.entities.User

data class MessageWithUser (
    @Embedded
    val message: Message,

    @Relation(parentColumn = "messageOwnerId", entityColumn = "id")
    val user: User
)