package com.akinci.chatter.feature.dashboard.data.output

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val avatarURL: String,
    val id: String,
    val nickname: String
)