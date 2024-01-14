package com.akinci.chatter.data.rest.user

import kotlinx.serialization.Serializable

@Serializable
data class InfoResponse(
    val seed: String,
    val results: Int,
    val page: Int,
    val version: String,
)
