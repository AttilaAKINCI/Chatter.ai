package com.akinci.chatter.domain.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatSession(
    val sessionId: Long,
    val chatMate: User,
) : Parcelable
