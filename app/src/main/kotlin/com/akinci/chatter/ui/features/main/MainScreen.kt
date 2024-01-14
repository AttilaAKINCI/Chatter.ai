package com.akinci.chatter.ui.features.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.akinci.chatter.ui.ds.components.systembarcontroller.SystemBarController
import com.akinci.chatter.ui.features.NavGraphs
import com.akinci.chatter.ui.features.appCurrentDestinationAsState
import com.akinci.chatter.ui.features.startAppDestination
import com.akinci.chatter.ui.navigation.getSystemBarColorState
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navHostEngine = rememberAnimatedNavHostEngine()

    val destination = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.root.startAppDestination

    // for each screen, we need to adjust system bars color & states
    SystemBarController(systemBarColorState = destination.getSystemBarColorState)

    DestinationsNavHost(
        navController = navController,
        engine = navHostEngine,
        navGraph = NavGraphs.root,
        startRoute = NavGraphs.root.startRoute,
    )
}
