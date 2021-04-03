package com.akinci.chatter.feature.dashboard.data.output

import com.akinci.chatter.feature.acommon.data.local.entities.MessageEntity
import com.akinci.chatter.feature.acommon.data.local.entities.UserEntity
import com.akinci.chatter.feature.acommon.data.local.entities.relations.MessageWithUser
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Message(
    val id: String,
    val text: String,
    val timestamp: Long,
    val user: User
)

/** For integrity between network and ROOM Database **/
fun List<Message>.convertMessageListToMessageEntityList(dataOwnerId: Long) : List<MessageEntity> {
    var messages = mutableListOf<MessageEntity>()

    apply {
        map {
            var messageEntity = MessageEntity(
                    id = it.id,
                    text = it.text,
                    timestamp = it.timestamp,
                    messageOwnerId = it.user.id.toLong(),
                    dataOwnerId = dataOwnerId
                )
            messages.add(messageEntity)
        }
    }
    return messages.toList()
}

fun List<Message>.convertMessageListToUserEntityList() : List<UserEntity> {
    val users = mutableListOf<UserEntity>()

    apply {
        map {
            val messageEntity = UserEntity(
                id = it.user.id.toLong(),
                loggedIdUser = false,
                avatarURL = it.user.avatarURL,
                nickname = it.user.nickname
            )
            users.add(messageEntity)
        }
    }
    return users.toList()
}