package com.akinci.chatter.ui.features.messaging

import android.os.Parcelable
import com.akinci.chatter.domain.chatwindow.ChatSession
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
        val messages: PersistentList<String> = persistentListOf(),
    )
}
