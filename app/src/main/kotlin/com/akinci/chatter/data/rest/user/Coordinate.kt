package com.akinci.chatter.data.rest.user

import kotlinx.serialization.Serializable

@Serializable
data class Coordinate(
    val latitude: String,
    val longitude: String,
)
