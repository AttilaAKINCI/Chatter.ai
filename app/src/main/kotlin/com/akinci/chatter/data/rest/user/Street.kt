package com.akinci.chatter.data.rest.user

import kotlinx.serialization.Serializable

@Serializable
data class Street(
    val number: Int,
    val name: String,
)
