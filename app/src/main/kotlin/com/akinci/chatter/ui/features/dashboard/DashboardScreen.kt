package com.akinci.chatter.ui.features.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.akinci.chatter.core.compose.UIModePreviews
import com.akinci.chatter.ui.ds.theme.ChatterTheme
import com.akinci.chatter.ui.features.NavGraphs
import com.akinci.chatter.ui.features.dashboard.DashboardViewContract.State
import com.akinci.chatter.ui.features.destinations.SplashScreenDestination
import com.akinci.chatter.ui.navigation.animation.FadeInOutAnimation
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo

// TODO list of users which chat before + Random user

@Composable
@Destination(style = FadeInOutAnimation::class)
fun DashboardScreen(
    navigator: DestinationsNavigator,
    vm: DashboardViewModel = hiltViewModel()
) {
    val uiState: State by vm.stateFlow.collectAsStateWithLifecycle()

    if (uiState.logoutUser) {
        navigator.navigate(SplashScreenDestination) {
            popUpTo(NavGraphs.root) { inclusive = true }
        }
    }

    DashboardScreenContent(
        onLogoutClick = { vm.logout() }
    )
}

@Composable
private fun DashboardScreenContent(
    onLogoutClick: () -> Unit,
) {
    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            Text(text = "DashboardScreen")
            Button(onClick = onLogoutClick) {
                Text(text = "Logout")
            }
        }
    }
}

@UIModePreviews
@Composable
private fun DashboardScreenPreview() {
    ChatterTheme {
        DashboardScreenContent(
            onLogoutClick = {},
        )
    }
}
