package com.akinci.chatter.data.rest.user

import kotlinx.serialization.Serializable

@Serializable
data class UserServiceResponse(
    val results: List<UserResponse>,
    val info: InfoResponse,
)
