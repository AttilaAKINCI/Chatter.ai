package com.akinci.chatter.domain.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Long,
    val name: String,
    val userName: String,
    val imageUrl: String,
    val phone: String,
    val nationality: String,
) : Parcelable
