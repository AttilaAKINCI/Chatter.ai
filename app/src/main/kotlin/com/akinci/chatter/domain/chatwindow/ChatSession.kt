package com.akinci.chatter.domain.chatwindow

import android.os.Parcelable
import com.akinci.chatter.domain.user.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatSession(
    val sessionId: Long,
    val chatMate: User,
) : Parcelable
