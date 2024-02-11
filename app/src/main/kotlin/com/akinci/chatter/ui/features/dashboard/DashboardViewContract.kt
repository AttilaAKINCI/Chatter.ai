package com.akinci.chatter.ui.features.dashboard

import androidx.annotation.StringRes
import com.akinci.chatter.core.compose.UIState
import com.akinci.chatter.domain.data.ChatSession
import com.akinci.chatter.domain.data.User
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

object DashboardViewContract {

    data class State(
        val stateType: StateType = StateType.LOADING,
        val chatSessions: PersistentList<ChatSession> = persistentListOf(),
        val loggedInUser: User? = null,
        val isNewChatButtonLoading: Boolean = false,
        val isLogoutDialogVisible: Boolean = false,
    )

    sealed interface Action {
        data object OnLogoutButtonClick : Action
        data object OnNewChatMateButtonClick : Action
    }

    sealed interface Effect {
        data object LogoutUser : Effect
        data class ShowToastMessage(@StringRes val messageId: Int) : Effect
    }

    enum class StateType {
        LOADING,
        NO_DATA,
        ERROR,
        CONTENT,
    }
}
