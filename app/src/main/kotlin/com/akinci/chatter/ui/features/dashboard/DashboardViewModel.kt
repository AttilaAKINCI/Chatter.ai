package com.akinci.chatter.ui.features.dashboard

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.chatter.R
import com.akinci.chatter.core.compose.reduce
import com.akinci.chatter.core.coroutine.ContextProvider
import com.akinci.chatter.data.datastore.DataStorage
import com.akinci.chatter.domain.chatwindow.ChatSessionUseCase
import com.akinci.chatter.domain.user.UserUseCase
import com.akinci.chatter.ui.ds.components.snackbar.SnackBarState
import com.akinci.chatter.ui.features.dashboard.DashboardViewContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val contextProvider: ContextProvider,
    private val dataStorage: DataStorage,
    private val chatSessionUseCase: ChatSessionUseCase,
    private val userUseCase: UserUseCase,
) : ViewModel() {
    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(State())
    val stateFlow = _stateFlow.asStateFlow()

    init {
        prepareInitialUIState()
    }

    private fun prepareInitialUIState() {
        viewModelScope.launch {
            withContext(contextProvider.io) {
                userUseCase.getLoggedInUser()
            }.onSuccess {
                // send logged in user info immediately to UI.
                _stateFlow.reduce { copy(loggedInUser = it) }
                // simulate delay for initial loading
                delay(1000)
                // subscribe to chat sessions and it's updates.
                subscribeToChatSessionUpdates(loggedInUserId = it.id)
            }.onFailure {
                // if logged in user name could not fetched, switch to error mode.
                _stateFlow.reduce {
                    copy(
                        chatSessions = persistentListOf(),
                        loading = false,
                        noData = false,
                        error = true,
                    )
                }
            }
        }
    }

    private fun subscribeToChatSessionUpdates(loggedInUserId: Long) {
        chatSessionUseCase.getChatSessions(memberId = loggedInUserId)
            .onEach { sessions ->
                if (sessions.isNotEmpty()) {
                    _stateFlow.reduce {
                        copy(
                            chatSessions = sessions.toPersistentList(),
                            loading = false,
                            noData = false,
                            error = false,
                        )
                    }
                } else {
                    _stateFlow.reduce {
                        copy(
                            chatSessions = persistentListOf(),
                            loading = false,
                            noData = true,
                            error = false,
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun findNewChatMate() {
        viewModelScope.launch {
            val loggedInUser = stateFlow.value.loggedInUser ?: return@launch

            withContext(contextProvider.io) {
                // generate new chat mate by fetching via REST endpoint
                userUseCase.getRandomUser()
                    .onSuccess { chatMate ->
                        // insert new chat mate
                        userUseCase.saveUser(chatMate)
                            .map { chatMate.copy(id = it) }
                            .onSuccess { savedUser ->
                                // create chat session
                                chatSessionUseCase.createChatSession(
                                    listOf(loggedInUser.id, savedUser.id)
                                ).onSuccess {
                                    // New chat session is successfully saved.
                                    sendSnackBarMessage(
                                        messageId = R.string.dashboard_screen_chat_session_successfully_created
                                    )
                                }.onFailure {
                                    // Chat session could not be saved.
                                    sendSnackBarMessage(
                                        messageId = R.string.dashboard_screen_chat_session_create_error
                                    )
                                }
                            }.onFailure {
                                // generated new chat mate could not be saved
                                sendSnackBarMessage(
                                    messageId = R.string.dashboard_screen_start_new_chat_mate_save_error
                                )
                            }
                    }.onFailure {
                        // REST endpoint return is failed.
                        sendSnackBarMessage(
                            messageId = R.string.dashboard_screen_start_new_chat_error
                        )
                    }
            }
        }
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

    private fun sendSnackBarMessage(@StringRes messageId: Int) {
        viewModelScope.launch {
            _stateFlow.reduce { copy(snackBarState = SnackBarState(messageId = messageId)) }
            delay(500)
            _stateFlow.reduce { copy(snackBarState = null) }
        }
    }
}
