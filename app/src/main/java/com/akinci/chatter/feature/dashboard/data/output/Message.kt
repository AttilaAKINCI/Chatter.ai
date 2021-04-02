package com.akinci.chatter.feature.dashboard.data.output

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Message(
    val id: String,
    val text: String,
    val timestamp: Int,
    val user: User
)