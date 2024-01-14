package com.akinci.chatter.ui.features.splash

import com.akinci.chatter.core.compose.UIState
import com.akinci.chatter.ui.features.destinations.DashboardScreenDestination
import com.akinci.chatter.ui.features.destinations.LoginScreenDestination
import com.akinci.chatter.ui.features.splash.SplashViewContract.Route

object SplashViewContract {
    data class State(
        val route: Route? = null
    ) : UIState

    enum class Route {
        Login,
        Dashboard,
    }
}

fun Route.toDestination() = when (this) {
    Route.Login -> LoginScreenDestination
    Route.Dashboard -> DashboardScreenDestination
}