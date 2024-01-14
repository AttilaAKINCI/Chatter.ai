package com.akinci.chatter.data.rest.user

import kotlinx.serialization.Serializable

@Serializable
data class TimeZone(
    val offset: String,
    val description: String,
)
