package com.akinci.chatter.domain.mapper

import com.akinci.chatter.data.rest.user.UserResponse
import com.akinci.chatter.data.room.user.UserEntity
import com.akinci.chatter.domain.user.User

fun UserEntity.toDomain() = User(
    id = id,
    name = name,
    userName = userName,
    imageUrl = imageUrl,
    phone = phone,
    nationality = nationality,
)

fun UserResponse.toDomain() = User(
    id = 0L,
    name = "${name.first} ${name.last}",
    userName = login.username,
    imageUrl = picture.large,
    phone = phone,
    nationality = nat,
)

fun User.toData() = UserEntity(
    name = name,
    userName = userName,
    imageUrl = imageUrl,
    phone = phone,
    nationality = nationality,
)
