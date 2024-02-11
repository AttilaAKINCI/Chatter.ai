package com.akinci.chatter.ui.features.messaging

import android.os.Parcelable
import com.akinci.chatter.core.compose.UIState
import com.akinci.chatter.domain.data.ChatSession
import com.akinci.chatter.domain.data.MessageItem
import com.akinci.chatter.domain.data.User
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize

object MessagingViewContract {

    @Parcelize
    data class ScreenArgs(val session: ChatSession) : Parcelable

    data class State(
        val session: ChatSession? = null,
        val loggedInUser: User? = null,
        val messages: PersistentList<MessageItem> = persistentListOf(),
        val text: String = "",
    ) : UIState

    sealed interface Action {
        data object OnSendButtonClick : Action
        data class OnTextChange(val text: String) : Action
    }

    sealed interface Effect
}
