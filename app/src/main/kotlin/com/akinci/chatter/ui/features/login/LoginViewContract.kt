package com.akinci.chatter.ui.features.login

import com.akinci.chatter.core.compose.UIState
import com.akinci.chatter.ui.ds.components.snackbar.SnackBarState

object LoginViewContract {
    data class State(
        val name: String = "",
        val validationError: Boolean = false,

        val isLoginButtonLoading: Boolean = false,
        val isRegisterButtonLoading: Boolean = false,

        val snackBarState: SnackBarState? = null,
        val navigateToDashboard: Boolean = false,
    ) : UIState
}