package com.akinci.chatter.ui.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.chatter.R
import com.akinci.chatter.core.compose.reduce
import com.akinci.chatter.core.coroutine.ContextProvider
import com.akinci.chatter.data.datastore.DataStorage
import com.akinci.chatter.data.exception.UserFetchError
import com.akinci.chatter.data.repository.UserRepository
import com.akinci.chatter.ui.ds.components.snackbar.SnackBarState
import com.akinci.chatter.ui.features.login.LoginViewContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
) : ViewModel() {
    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(State())
    val stateFlow = _stateFlow.asStateFlow()

    fun updateName(name: String) {
        _stateFlow.reduce {
            copy(
                name = name,
                validationError = false,
            )
        }
    }

    fun tryToLogin() {
        if (!validateInputs()) return

        viewModelScope.launch {
            // delay for proper loading animation on UI.
            _stateFlow.reduce {
                copy(
                    validationError = false,
                    isLoginButtonLoading = true,
                )
            }
            delay(500L)

            // try verify user details on local storage
            if (verifyUser()) {
                // to simulate successful login save user's name to local storage (auto log in)
                login()

                // navigate user to dashboard screen.
                _stateFlow.reduce {
                    copy(
                        navigateToDashboard = true,
                        isLoginButtonLoading = false,
                        isRegisterButtonLoading = false,
                    )
                }
            } else {
                // provided user's name couldn't found on local storage.
                _stateFlow.reduce {
                    copy(
                        snackBarState = SnackBarState(
                            messageId = R.string.login_screen_error_user_could_not_found_in_local
                        ),
                        isLoginButtonLoading = false,
                        isRegisterButtonLoading = false,
                    )
                }
            }
        }
    }

    fun tryToRegister() {
        if (!validateInputs()) return

        viewModelScope.launch {
            // delay for proper loading animation on UI.
            _stateFlow.reduce {
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
                        it.copy(name = stateFlow.value.name)
                    }
                }.onFailure {
                    // our random user creation rest call is failed.
                    val errorMessageId = when (it) {
                        is SocketTimeoutException,
                        is UnknownHostException -> R.string.general_connection_problem

                        is UserFetchError -> R.string.login_screen_error_user_generation

                        else -> R.string.login_screen_error_general
                    }

                    _stateFlow.reduce {
                        copy(
                            snackBarState = SnackBarState(messageId = errorMessageId),
                            isLoginButtonLoading = false,
                            isRegisterButtonLoading = false,
                        )
                    }
                    return@launch
                }.getOrNull()

                if (user != null) {
                    withContext(contextProvider.io) {
                        userRepository.create(user)
                    }.onSuccess {
                        // we successfully created and saved new user.
                        // to simulate successful login save user's name to local storage (auto log in)
                        login()

                        // navigate user to dashboard screen.
                        _stateFlow.reduce {
                            copy(
                                navigateToDashboard = true,
                                isLoginButtonLoading = false,
                                isRegisterButtonLoading = false,
                            )
                        }
                    }.onFailure {
                        // we couldn't save created user to our local storage.
                        _stateFlow.reduce {
                            copy(
                                snackBarState = SnackBarState(
                                    messageId = R.string.login_screen_error_user_saved
                                ),
                                isLoginButtonLoading = false,
                                isRegisterButtonLoading = false,
                            )
                        }
                    }
                } else {
                    // we received empty rest response.
                    _stateFlow.reduce {
                        copy(
                            snackBarState = SnackBarState(
                                messageId = R.string.login_screen_error_user_blank_response
                            ),
                            isLoginButtonLoading = false,
                            isRegisterButtonLoading = false,
                        )
                    }
                }
            } else {
                // we found this user in our local storage. We can not create again.
                _stateFlow.reduce {
                    copy(
                        snackBarState = SnackBarState(
                            messageId = R.string.login_screen_error_user_already_exists
                        ),
                        isLoginButtonLoading = false,
                        isRegisterButtonLoading = false,
                    )
                }
            }
        }
    }

    private suspend fun login() {
        withContext(contextProvider.io) {
            dataStorage.setLoggedInUsersName(stateFlow.value.name)
        }
    }

    private suspend fun verifyUser(): Boolean {
        return withContext(contextProvider.io) {
            userRepository.get(stateFlow.value.name).getOrNull() != null
        }
    }

    private fun validateInputs() = stateFlow.value.name.isNotEmpty().also { isValid ->
        if (!isValid) {
            _stateFlow.reduce {
                copy(
                    validationError = true,
                    isLoginButtonLoading = false,
                    isRegisterButtonLoading = false,
                )
            }
        }
    }
}
