package com.akinci.chatter.data.rest.user

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val gender: String,
    val name: Name,
    val location: Location,
    val email: String,
    val login: LoginInfo,
    val dob: Dob,
    val registered: Dob,
    val phone: String,
    val cell: String,
    val id: Id,
    val picture: Picture,
    val nat: String,
)
