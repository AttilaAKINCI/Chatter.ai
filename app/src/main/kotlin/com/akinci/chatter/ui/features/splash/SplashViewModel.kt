package com.akinci.chatter.ui.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.chatter.core.coroutine.ContextProvider
import com.akinci.chatter.data.datastore.DataStorage
import com.akinci.chatter.ui.features.splash.SplashViewContract.Effect
import com.akinci.chatter.ui.features.splash.SplashViewContract.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val contextProvider: ContextProvider,
    private val dataStorage: DataStorage,
) : ViewModel() {

    private val _effect by lazy { Channel<Effect>() }
    val effect: Flow<Effect> by lazy { _effect.receiveAsFlow() }

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
        _effect.send(Effect.NavigateToRoute(route = route))
    }
}
