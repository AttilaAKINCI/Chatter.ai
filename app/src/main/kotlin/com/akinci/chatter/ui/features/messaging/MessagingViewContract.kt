package com.akinci.chatter.ui.features.messaging

import android.os.Parcelable
import com.akinci.chatter.core.compose.UIState
import com.akinci.chatter.domain.chatwindow.ChatSession
import com.akinci.chatter.domain.message.MessageItem
import com.akinci.chatter.domain.user.User
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize

object MessagingViewContract {

    @Parcelize
    data class ScreenArgs(
        val session: ChatSession,
    ) : Parcelable

    data class State(
        val session: ChatSession,
        val loggedInUser: User? = null,
        val messages: PersistentList<MessageItem> = persistentListOf(),
        val text: String = "",
    ) : UIState
}
