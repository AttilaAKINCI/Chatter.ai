package com.akinci.chatter.ui.features.dashboard

import com.akinci.chatter.core.compose.UIState

object DashboardViewContract {

    data class State(
        val logoutUser: Boolean = false,
    ) : UIState
}