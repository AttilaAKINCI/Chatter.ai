package com.akinci.chatter.data.rest.user

import kotlinx.serialization.Serializable

@Serializable
data class Dob(
    val date: String,
    val age: Int,
)
