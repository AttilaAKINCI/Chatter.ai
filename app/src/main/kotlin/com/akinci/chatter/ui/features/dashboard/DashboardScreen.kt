package com.akinci.chatter.ui.features.dashboard

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.akinci.chatter.R
import com.akinci.chatter.core.compose.UIModePreviews
import com.akinci.chatter.domain.chatwindow.ChatSession
import com.akinci.chatter.ui.ds.components.CachedImage
import com.akinci.chatter.ui.ds.components.Dialog
import com.akinci.chatter.ui.ds.components.LoadingButton
import com.akinci.chatter.ui.ds.components.snackbar.SnackBarContainer
import com.akinci.chatter.ui.ds.theme.ChatterTheme
import com.akinci.chatter.ui.ds.theme.DarkYellow
import com.akinci.chatter.ui.ds.theme.bodyLarge_swash
import com.akinci.chatter.ui.ds.theme.oval
import com.akinci.chatter.ui.features.NavGraphs
import com.akinci.chatter.ui.features.dashboard.DashboardViewContract.State
import com.akinci.chatter.ui.features.destinations.MessagingScreenDestination
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
        onNewChatMateButtonClick = { vm.findNewChatMate() },
        onChatSessionClick = { session ->
            navigator.navigate(
                MessagingScreenDestination(session = session)
            )
        }
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
    onChatSessionClick: (ChatSession) -> Unit,
) {
    Surface {
        SnackBarContainer(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.navigationBars)
                .fillMaxSize(),
            snackBarState = uiState.snackBarState,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                DashboardScreen.TopBar(
                    name = uiState.loggedInUser?.name.orEmpty(),
                    onLogoutClick = onLogoutClick,
                )

                when {
                    uiState.error -> DashboardScreen.Info(
                        modifier = Modifier.weight(1f),
                        text = stringResource(id = R.string.dashboard_screen_error),
                        image = painterResource(id = R.drawable.ic_error_128dp),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )

                    uiState.noData -> DashboardScreen.Info(
                        modifier = Modifier.weight(1f),
                        text = stringResource(id = R.string.dashboard_screen_no_data),
                        image = painterResource(id = R.drawable.ic_no_data_128dp),
                        tint = Color.DarkYellow,
                    )

                    uiState.loading -> DashboardScreen.Loading(modifier = Modifier.weight(1f))

                    else -> DashboardScreen.Content(
                        modifier = Modifier.weight(1f),
                        sessions = uiState.chatSessions,
                        onClick = onChatSessionClick,
                    )
                }

                DashboardScreen.Footer(
                    isLoading = uiState.isNewChatButtonLoading,
                    onNewChatMateButtonClick = onNewChatMateButtonClick,
                )
            }
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
                text = buildString {
                    append(stringResource(id = R.string.dashboard_screen_top_bar_title))
                    if (name.isNotBlank()) append(", $name")
                    append(stringResource(id = R.string.dashboard_screen_top_bar_title_tail))
                },
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
private fun DashboardScreen.Info(
    modifier: Modifier = Modifier,
    image: Painter,
    tint: Color,
    text: String,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = image,
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = tint)
            )
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun DashboardScreen.Loading(
    modifier: Modifier = Modifier,
) {
    // TODO update loading into shimmer loading
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun DashboardScreen.Content(
    modifier: Modifier = Modifier,
    sessions: PersistentList<ChatSession>,
    onClick: (ChatSession) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        state = rememberLazyListState()
    ) {
        items(sessions) { session ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        indication = rememberRipple(),
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { onClick(session) },
                    )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CachedImage(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(64.dp)
                            .clip(MaterialTheme.shapes.oval),
                        imageUrl = session.chatMate.imageUrl
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = session.chatMate.name,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }

                    Icon(
                        modifier = Modifier.padding(16.dp),
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                    )
                }

                Divider()
            }
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
            onChatSessionClick = {},
        )
    }
}
