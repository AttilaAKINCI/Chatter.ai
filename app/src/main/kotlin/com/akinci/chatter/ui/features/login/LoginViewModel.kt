package com.akinci.chatter.ui.features.login

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.chatter.R
import com.akinci.chatter.core.coroutine.ContextProvider
import com.akinci.chatter.core.mvi.MVI
import com.akinci.chatter.core.mvi.mvi
import com.akinci.chatter.data.datastore.DataStorage
import com.akinci.chatter.data.exception.UserFetchError
import com.akinci.chatter.data.repository.UserRepository
import com.akinci.chatter.ui.features.login.LoginViewContract.Action
import com.akinci.chatter.ui.features.login.LoginViewContract.Effect
import com.akinci.chatter.ui.features.login.LoginViewContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val contextProvider: ContextProvider,
    private val dataStorage: DataStorage,
    private val userRepository: UserRepository,
) : ViewModel(), MVI<State, Action, Effect> by mvi(State()) {

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnTextChange -> updateName(action.text)
            Action.OnLoginButtonClick -> tryToLogin()
            Action.OnRegisterButtonClick -> tryToRegister()
        }
    }

    fun updateName(name: String) {
        updateState {
            copy(
                name = name,
                validationError = false
            )
        }
    }

    fun tryToLogin() {
        if (!validateInputs()) return

        viewModelScope.launch {
            // delay for proper loading animation on UI.
            updateState {
                copy(
                    validationError = false,
                    isLoginButtonLoading = true
                )
            }
            delay(500L)

            // try verify user details on local storage
            if (verifyUser()) {
                // to simulate successful login save user's name to local storage (auto log in)
                login()

                // send navigate to dashboard effect
                sendEffect(Effect.NavigateToDashboard)
            } else {
                // provided user's name couldn't found on local storage.
                sendToastMessage(messageId = R.string.login_screen_error_user_could_not_found_in_local)
            }
        }
    }

    fun tryToRegister() {
        if (!validateInputs()) return

        viewModelScope.launch {
            // delay for proper loading animation on UI.
            updateState {
                copy(
                    validationError = false,
                    isRegisterButtonLoading = true
                )
            }
            delay(500L)

            // try to verify user details on local storage
            if (!verifyUser()) {
                // user couldn't found in local database so we can create new one and replace user's name
                val user = withContext(contextProvider.io) {
                    userRepository.generateRandomUser().map {
                        it.copy(name = state.value.name)
                    }
                }.onFailure {
                    // our random user creation rest call is failed.
                    val errorMessageId = when (it) {
                        is SocketTimeoutException,
                        is UnknownHostException -> R.string.general_connection_problem

                        is UserFetchError -> R.string.login_screen_error_user_generation

                        else -> R.string.login_screen_error_general
                    }

                    sendToastMessage(messageId = errorMessageId)

                    return@launch
                }.getOrNull()

                if (user != null) {
                    withContext(contextProvider.io) {
                        userRepository.create(user)
                    }.onSuccess {
                        // we successfully created and saved new user.
                        // to simulate successful login save user's name to local storage (auto log in)
                        login()

                        // send navigate to dashboard effect
                        sendEffect(Effect.NavigateToDashboard)
                    }.onFailure {
                        // we couldn't save created user to our local storage.
                        sendToastMessage(messageId = R.string.login_screen_error_user_saved)
                    }
                } else {
                    // we received empty rest response.
                    sendToastMessage(messageId = R.string.login_screen_error_user_blank_response)
                }
            } else {
                // we found this user in our local storage. We can not create again.
                sendToastMessage(messageId = R.string.login_screen_error_user_already_exists)
            }
        }
    }

    private suspend fun sendToastMessage(@StringRes messageId: Int) {
        // restore loading button states
        updateState {
            copy(
                isLoginButtonLoading = false,
                isRegisterButtonLoading = false,
            )
        }

        // show toast error message
        sendEffect(
            Effect.ShowToastMessage(messageId = messageId)
        )
    }

    private suspend fun login() {
        withContext(contextProvider.io) {
            dataStorage.setLoggedInUsersName(state.value.name)
        }
    }

    private suspend fun verifyUser(): Boolean {
        return withContext(contextProvider.io) {
            userRepository.get(state.value.name).getOrNull() != null
        }
    }

    private fun validateInputs() = state.value.name.isNotEmpty().also { isValid ->
        if (!isValid) {
            updateState {
                copy(
                    validationError = true,
                    isLoginButtonLoading = false,
                    isRegisterButtonLoading = false,
                )
            }
        }
    }
}
