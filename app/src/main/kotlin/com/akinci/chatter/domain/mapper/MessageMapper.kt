package com.akinci.chatter.domain.mapper

import com.akinci.chatter.data.room.message.MessageEntity
import com.akinci.chatter.domain.message.Message
import java.time.ZonedDateTime

fun MessageEntity.toDomain(sender: Long) = Message(
    isMine = this.sender == sender,
    time = runCatching { ZonedDateTime.parse(time) }.getOrNull(),
    text = text
)