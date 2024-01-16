package com.akinci.chatter.ui.features.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.chatter.core.compose.reduce
import com.akinci.chatter.data.datastore.DataStorage
import com.akinci.chatter.ui.features.dashboard.DashboardViewContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dataStorage: DataStorage,
) : ViewModel() {
    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(State())
    val stateFlow = _stateFlow.asStateFlow()

    init {
        prepareInitialUIState()
    }

    private fun prepareInitialUIState() {
        viewModelScope.launch {
            val loggedInUsersName = dataStorage.getLoggedInUsersName()

            _stateFlow.reduce {
                copy(
                    name = loggedInUsersName.orEmpty(),
                    users = persistentListOf()
                )
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
