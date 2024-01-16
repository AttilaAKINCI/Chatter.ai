package com.akinci.chatter.ui.features.dashboard

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.akinci.chatter.R
import com.akinci.chatter.core.compose.UIModePreviews
import com.akinci.chatter.domain.user.User
import com.akinci.chatter.ui.ds.components.Dialog
import com.akinci.chatter.ui.ds.components.LoadingButton
import com.akinci.chatter.ui.ds.theme.ChatterTheme
import com.akinci.chatter.ui.ds.theme.bodyLarge_swash
import com.akinci.chatter.ui.features.NavGraphs
import com.akinci.chatter.ui.features.dashboard.DashboardViewContract.State
import com.akinci.chatter.ui.features.destinations.SplashScreenDestination
import com.akinci.chatter.ui.navigation.animation.FadeInOutAnimation
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import kotlinx.collections.immutable.PersistentList

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

    // we need to disable back button actions for this screen.
    BackHandler(enabled = true) {
        // consume back action.
    }

    DashboardScreenContent(
        uiState = uiState,
        onLogoutClick = { vm.showLogoutDialog() },
        onNewChatMateButtonClick = { vm.findNewChatMate() }
    )

    AnimatedVisibility(visible = uiState.isLogoutDialogVisible) {
        Dialog(
            title = stringResource(id = R.string.dashboard_screen_log_out_confirm_title),
            description = stringResource(id = R.string.dashboard_screen_log_out_confirm_description),
            confirmButtonText = stringResource(id = R.string.dashboard_screen_log_out_confirm_button_title),
            dismissButtonText = stringResource(id = R.string.dashboard_screen_log_out_dismiss_button_title),
            onConfirmButtonClick = { vm.logout() },
            onDismissButtonClick = { vm.hideLogoutDialog() },
        )
    }
}

@Composable
private fun DashboardScreenContent(
    uiState: State,
    onLogoutClick: () -> Unit,
    onNewChatMateButtonClick: () -> Unit,
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            DashboardScreen.TopBar(
                name = uiState.name,
                onLogoutClick = onLogoutClick,
            )

            DashboardScreen.Content(
                modifier = Modifier.weight(1f),
                users = uiState.users,
            )

            DashboardScreen.Footer(
                isLoading = uiState.isNewChatButtonLoading,
                onNewChatMateButtonClick = onNewChatMateButtonClick,
            )
        }
    }
}

typealias DashboardScreen = Unit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreen.TopBar(
    name: String,
    onLogoutClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.dashboard_screen_top_bar_title, name),
                style = MaterialTheme.typography.bodyLarge_swash
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        actions = {
            IconButton(onClick = onLogoutClick) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = null
                )
            }
        },
    )
    Divider()
}

@Composable
private fun DashboardScreen.Content(
    modifier: Modifier = Modifier,
    users: PersistentList<User>,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        state = rememberLazyListState()
    ) {
        items(users) {
            // TODO apply messaging rows.
        }
    }
}

@Composable
private fun DashboardScreen.Footer(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onNewChatMateButtonClick: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        LoadingButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            title = stringResource(id = R.string.dashboard_screen_new_chat_button_title),
            isLoading = isLoading,
            onClick = onNewChatMateButtonClick
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@UIModePreviews
@Composable
private fun DashboardScreenPreview() {
    ChatterTheme {
        DashboardScreenContent(
            uiState = State(),
            onLogoutClick = {},
            onNewChatMateButtonClick = {},
        )
    }
}
