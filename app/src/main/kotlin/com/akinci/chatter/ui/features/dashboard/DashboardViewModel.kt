package com.akinci.chatter.ui.features.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.chatter.R
import com.akinci.chatter.core.coroutine.ContextProvider
import com.akinci.chatter.core.mvi.MVI
import com.akinci.chatter.core.mvi.mvi
import com.akinci.chatter.data.datastore.DataStorage
import com.akinci.chatter.data.repository.ChatSessionRepository
import com.akinci.chatter.data.repository.UserRepository
import com.akinci.chatter.domain.GetPrimaryUserUseCase
import com.akinci.chatter.ui.features.dashboard.DashboardViewContract.Action
import com.akinci.chatter.ui.features.dashboard.DashboardViewContract.Effect
import com.akinci.chatter.ui.features.dashboard.DashboardViewContract.State
import com.akinci.chatter.ui.features.dashboard.DashboardViewContract.StateType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val contextProvider: ContextProvider,
    private val dataStorage: DataStorage,
    private val chatSessionRepository: ChatSessionRepository,
    private val userRepository: UserRepository,
    private val getPrimaryUserUseCase: GetPrimaryUserUseCase,
) : ViewModel(), MVI<State, Action, Effect> by mvi(State()) {

    override fun onAction(action: Action) {
        when(action){
            Action.OnLogoutButtonClick -> showLogoutDialog()
            Action.OnNewChatMateButtonClick -> findNewChatMate()
        }
    }

    init {
        prepareInitialUIState()
    }

    private fun prepareInitialUIState() {
        viewModelScope.launch {
            withContext(contextProvider.io) {
                getPrimaryUserUseCase.execute()
            }.onSuccess {
                // send logged in user info immediately to UI.
                updateState { copy(loggedInUser = it) }
                // simulate delay for initial loading
                delay(1000)
                // subscribe to chat sessions and it's updates.
                subscribeToChatSessionUpdates(loggedInUserId = it.id)
            }.onFailure {
                // if logged in user name could not fetched, switch to error mode.
                updateState {
                    copy(
                        stateType = StateType.ERROR,
                        chatSessions = persistentListOf(),
                    )
                }
            }
        }
    }

    private fun subscribeToChatSessionUpdates(loggedInUserId: Long) {
        chatSessionRepository.getChatSessionStream(memberId = loggedInUserId)
            .onEach { sessions ->
                if (sessions.isNotEmpty()) {
                    updateState {
                        copy(
                            stateType = StateType.CONTENT,
                            chatSessions = sessions.toPersistentList(),
                        )
                    }
                } else {
                    updateState {
                        copy(
                            stateType = StateType.NO_DATA,
                            chatSessions = persistentListOf(),
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun findNewChatMate() {
        viewModelScope.launch {
            val loggedInUser = state.value.loggedInUser ?: return@launch

            withContext(contextProvider.io) {
                // generate new chat mate by fetching via REST endpoint
                userRepository.generateRandomUser()
                    .onSuccess { chatMate ->
                        // create new chat mate
                        userRepository.create(chatMate)
                            .map { chatMate.copy(id = it) }
                            .onSuccess { savedUser ->
                                // create chat session
                                chatSessionRepository.create(
                                    primaryUserId = loggedInUser.id,
                                    secondaryUserId = savedUser.id
                                ).onSuccess {
                                    // New chat session is successfully saved.
                                    sendEffect(
                                        Effect.ShowToastMessage(
                                            messageId = R.string.dashboard_screen_chat_session_successfully_created
                                        )
                                    )
                                }.onFailure {
                                    // Chat session could not be saved.
                                    sendEffect(
                                        Effect.ShowToastMessage(
                                            messageId = R.string.dashboard_screen_chat_session_create_error
                                        )
                                    )
                                }
                            }.onFailure {
                                // generated new chat mate could not be saved
                                sendEffect(
                                    Effect.ShowToastMessage(
                                        messageId = R.string.dashboard_screen_start_new_chat_mate_save_error
                                    )
                                )
                            }
                    }.onFailure {
                        // REST endpoint return is failed.
                        sendEffect(
                            Effect.ShowToastMessage(
                                messageId = R.string.dashboard_screen_start_new_chat_error
                            )
                        )
                    }
            }
        }
    }

    fun showLogoutDialog() {
        updateState { copy(isLogoutDialogVisible = true) }
    }

    fun hideLogoutDialog() {
        updateState { copy(isLogoutDialogVisible = false) }
    }

    fun logout() {
        viewModelScope.launch {
            // give some time to logout dialog to vanish with animation
            hideLogoutDialog()
            delay(750L)

            // clear logged in user's name from storage
            dataStorage.setLoggedInUsersName("")
            // restart application
            sendEffect(Effect.LogoutUser)
        }
    }
}
