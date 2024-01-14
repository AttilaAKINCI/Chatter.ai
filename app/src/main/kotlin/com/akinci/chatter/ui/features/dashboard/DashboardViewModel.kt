package com.akinci.chatter.ui.features.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.akinci.chatter.core.compose.reduce
import com.akinci.chatter.data.datastore.DataStorage
import com.akinci.chatter.ui.features.dashboard.DashboardViewContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun logout() {
        viewModelScope.launch {
            // clear logged in user's name from storage
            dataStorage.setLoggedInUsersName("")
            // restart application
            _stateFlow.reduce{ copy(logoutUser = true) }
        }
    }
}
