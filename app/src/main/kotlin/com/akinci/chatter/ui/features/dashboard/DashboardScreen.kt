package com.akinci.chatter.ui.features.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.akinci.chatter.core.compose.UIModePreviews
import com.akinci.chatter.ui.ds.theme.ChatterTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun DashboardScreen(
    navigator: DestinationsNavigator,
) {
    // TODO list of users which chat before + Random user

    DashboardScreenContent()
}

@Composable
private fun DashboardScreenContent() {
    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            Text(text = "DashboardScreen")
        }
    }
}

@UIModePreviews
@Composable
private fun DashboardScreenPreview() {
    ChatterTheme {
        DashboardScreenContent()
    }
}