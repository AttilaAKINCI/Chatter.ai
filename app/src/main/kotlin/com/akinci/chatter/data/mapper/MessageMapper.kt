package com.akinci.chatter.data.mapper

import com.akinci.chatter.data.room.message.MessageWithUser
import com.akinci.chatter.domain.data.Message

fun MessageWithUser.toDomain() = Message(
    id = messageEntity.id,
    sender = userEntity.toDomain(),
    text = messageEntity.text,
    date = messageEntity.date
)