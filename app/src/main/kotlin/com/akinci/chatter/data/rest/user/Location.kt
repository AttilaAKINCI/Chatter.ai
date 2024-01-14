package com.akinci.chatter.data.rest.user

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val street: Street,
    val city: String,
    val state: String,
    val country: String,
    val postcode: Int,
    val coordinates: Coordinate,
    val timezone: TimeZone,
)
