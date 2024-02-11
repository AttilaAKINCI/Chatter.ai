package com.akinci.chatter.ui.features.splash

import com.akinci.chatter.ui.features.destinations.DashboardScreenDestination
import com.akinci.chatter.ui.features.destinations.LoginScreenDestination
import com.akinci.chatter.ui.features.splash.SplashViewContract.Route

object SplashViewContract {
    sealed interface Effect {
        data class NavigateToRoute(val route: Route) : Effect
    }

    enum class Route {
        Login,
        Dashboard,
    }
}

fun Route.toDestination() = when (this) {
    Route.Login -> LoginScreenDestination
    Route.Dashboard -> DashboardScreenDestination
}