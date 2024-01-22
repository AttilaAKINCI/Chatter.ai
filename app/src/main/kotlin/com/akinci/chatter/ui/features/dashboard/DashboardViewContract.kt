package com.akinci.chatter.ui.features.dashboard

import com.akinci.chatter.core.compose.UIState
import com.akinci.chatter.domain.chatwindow.ChatSession
import com.akinci.chatter.domain.user.User
import com.akinci.chatter.ui.ds.components.snackbar.SnackBarState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

object DashboardViewContract {

    data class State(
        val loggedInUser: User? = null,
        val chatSessions: PersistentList<ChatSession> = persistentListOf(),
        val loading: Boolean = true,
        val noData: Boolean = false,
        val error: Boolean = false,
        val isNewChatButtonLoading: Boolean = false,
        val logoutUser: Boolean = false,
        val isLogoutDialogVisible: Boolean = false,

        val snackBarState: SnackBarState? = null,
    ) : UIState
}
