package com.akinci.chatter.ui.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.chatter.core.compose.reduce
import com.akinci.chatter.core.coroutine.ContextProvider
import com.akinci.chatter.data.datastore.DataStorage
import com.akinci.chatter.ui.features.splash.SplashViewContract.Route
import com.akinci.chatter.ui.features.splash.SplashViewContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val contextProvider: ContextProvider,
    private val dataStorage: DataStorage,
) : ViewModel() {
    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(State())
    val stateFlow = _stateFlow.asStateFlow()

    init {
        initialize()
    }

    private fun initialize() {
        viewModelScope.launch {
            // simulate initialization with delay.
            delay(3000)

            // proceed to next screen
            decideLandingScreen()
        }
    }

    private suspend fun decideLandingScreen() {
        val name = withContext(contextProvider.io) {
            dataStorage.getLoggedInUsersName()
        }

        val route = when {
            // user logged in, navigate to dashboard
            !name.isNullOrEmpty() -> Route.Dashboard
            // user is not logged in, navigate to login screen
            else -> Route.Login
        }

        // complete initial animation, proceed dashboard.
        _stateFlow.reduce {
            copy(route = route)
        }
    }
}
