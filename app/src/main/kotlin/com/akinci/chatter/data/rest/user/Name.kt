package com.akinci.chatter.data.rest.user

import kotlinx.serialization.Serializable

@Serializable
data class Name(
    val title: String,
    val first: String,
    val last: String,
)
