package com.akinci.chatter.ui.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.chatter.R
import com.akinci.chatter.core.compose.reduce
import com.akinci.chatter.core.coroutine.ContextProvider
import com.akinci.chatter.data.datastore.DataStorage
import com.akinci.chatter.domain.user.UserUseCase
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
    private val userUseCase: UserUseCase,
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

            val loginName = stateFlow.value.name
            if (userUseCase.verifyUser(loginName)) {
                // user details on local storage is verified
                // save user's name to local storage for auto login feature.
                dataStorage.setLoggedInUsersName(loginName)

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

            // create random user and replace user's name
            val loginName = stateFlow.value.name
            if (!userUseCase.verifyUser(loginName)) {
                // user couldn't found in local database so we can create new one.
                val user = withContext(contextProvider.io) {
                    userUseCase.getRandomUser()
                        .map { it?.copy(name = loginName) }
                }.onFailure {
                    // our random user creation rest call is failed.
                    val errorMessageId = when (it) {
                        is SocketTimeoutException,
                        is UnknownHostException -> R.string.general_connection_problem

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
                    userUseCase.saveUser(user)
                        .onSuccess {
                            // we successfully created and saved new user.
                            // save user's name to local storage for auto login feature.
                            dataStorage.setLoggedInUsersName(loginName)

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
