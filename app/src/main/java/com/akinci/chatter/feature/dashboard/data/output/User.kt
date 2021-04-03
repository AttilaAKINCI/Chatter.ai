package com.akinci.chatter.feature.dashboard.data.output

import com.akinci.chatter.feature.acommon.data.local.entities.UserEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val id: String,
    val avatarURL: String,
    val nickname: String
)

/** For integrity between network and ROOM Database **/
fun User.convertUserToUserEntity() : UserEntity {
    var userEntity : UserEntity

    apply {
        userEntity = UserEntity(
           id = this.id.toLong(),
           loggedIdUser = false,
           avatarURL = this.avatarURL,
           nickname = this.nickname
       )
    }

    return userEntity
}