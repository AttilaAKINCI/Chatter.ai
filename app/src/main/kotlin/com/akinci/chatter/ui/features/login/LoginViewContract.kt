package com.akinci.chatter.ui.features.login

import androidx.annotation.StringRes

object LoginViewContract {

    data class State(
        val name: String = "",
        val validationError: Boolean = false,
        val isLoginButtonLoading: Boolean = false,
        val isRegisterButtonLoading: Boolean = false,
    )

    sealed interface Action {
        data class OnTextChange(val text: String) : Action
        data object OnLoginButtonClick : Action
        data object OnRegisterButtonClick : Action
    }

    sealed interface Effect {
        data class ShowToastMessage(@StringRes val messageId: Int) : Effect
        data object NavigateToDashboard : Effect
    }
}
