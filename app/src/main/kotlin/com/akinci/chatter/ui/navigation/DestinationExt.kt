package com.akinci.chatter.ui.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.akinci.chatter.ui.ds.components.systembarcontroller.SystemBarColorState
import com.akinci.chatter.ui.features.destinations.Destination

val Destination.getSystemBarColorState
    @Composable
    get() = SystemBarColorState(
        statusBarColor = MaterialTheme.colorScheme.surface,
        navigationBarColor = MaterialTheme.colorScheme.surface,
    )