package com.akinci.chatter.domain.message

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@Parcelize
data class Message(
    val isMine: Boolean,
    val time: ZonedDateTime?,
    val text: String,
) : Parcelable
