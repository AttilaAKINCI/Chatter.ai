package com.akinci.chatter.ui.features.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.chatter.core.compose.reduce
import com.akinci.chatter.core.coroutine.ContextProvider
import com.akinci.chatter.data.datastore.DataStorage
import com.akinci.chatter.domain.user.UserUseCase
import com.akinci.chatter.ui.features.dashboard.DashboardViewContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val contextProvider: ContextProvider,
    private val dataStorage: DataStorage,
    private val userUseCase: UserUseCase,
) : ViewModel() {
    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(State())
    val stateFlow = _stateFlow.asStateFlow()

    init {
        prepareInitialUIState()
    }

    private fun prepareInitialUIState() {
        viewModelScope.launch {
            val loggedInUsersName = withContext(contextProvider.io) {
                dataStorage.getLoggedInUsersName()
            }

            if (loggedInUsersName == null) {
                // if logged in user name could not fetched, switch to error mode.
                _stateFlow.reduce {
                    copy(
                        users = persistentListOf(),
                        loading = false,
                        noData = false,
                        error = true,
                    )
                }
            } else {
                // send logged in user name immediately
                _stateFlow.reduce { copy(name = loggedInUsersName) }

                withContext(contextProvider.io) {
                    userUseCase.getChatMembers(loggedInUsersName)
                }.onSuccess { receivers ->
                    if (receivers.isNotEmpty()) {
                        _stateFlow.reduce {
                            copy(
                                users = receivers.toPersistentList(),
                                loading = false,
                                noData = false,
                                error = false,
                            )
                        }
                    } else {
                        _stateFlow.reduce {
                            copy(
                                users = persistentListOf(),
                                loading = false,
                                noData = true,
                                error = false,
                            )
                        }
                    }
                }.onFailure {
                    _stateFlow.reduce {
                        copy(
                            users = persistentListOf(),
                            loading = false,
                            noData = false,
                            error = true,
                        )
                    }
                }
            }
        }
    }

    fun findNewChatMate() {

    }

    fun showLogoutDialog() {
        _stateFlow.reduce { copy(isLogoutDialogVisible = true) }
    }

    fun hideLogoutDialog() {
        _stateFlow.reduce { copy(isLogoutDialogVisible = false) }
    }

    fun logout() {
        viewModelScope.launch {
            // give some time to logout dialog to vanish with animation
            hideLogoutDialog()
            delay(750L)

            // clear logged in user's name from storage
            dataStorage.setLoggedInUsersName("")
            // restart application
            _stateFlow.reduce { copy(logoutUser = true) }
        }
    }
}
