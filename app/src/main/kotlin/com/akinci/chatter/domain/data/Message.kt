package com.akinci.chatter.domain.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    val id: Long,
    val sender: User,
    val date: String,
    val text: String,
) : Parcelable
