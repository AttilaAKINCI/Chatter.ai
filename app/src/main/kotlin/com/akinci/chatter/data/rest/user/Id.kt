package com.akinci.chatter.data.rest.user

import kotlinx.serialization.Serializable

@Serializable
data class Id(
    val name: String,
    val value: String?,
)
