package com.akinci.chatter.ui.features.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.akinci.chatter.R
import com.akinci.chatter.core.compose.UIModePreviews
import com.akinci.chatter.ui.ds.components.InfiniteLottieAnimation
import com.akinci.chatter.ui.ds.components.LoadingButton
import com.akinci.chatter.ui.ds.components.snackbar.SnackBarContainer
import com.akinci.chatter.ui.ds.theme.ChatterTheme
import com.akinci.chatter.ui.features.destinations.DashboardScreenDestination
import com.akinci.chatter.ui.features.login.LoginViewContract.State
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
    vm: LoginViewModel = hiltViewModel(),
) {
    val uiState: State by vm.stateFlow.collectAsStateWithLifecycle()

    if (uiState.navigateToDashboard) {
        navigator.navigate(DashboardScreenDestination)
    }

    // we need to disable back button actions for this screen.
    BackHandler(enabled = true) {
        // consume back action.
    }

    LoginScreenContent(
        uiState = uiState,
        onNameChange = { vm.updateName(it) },
        onLoginButtonClick = { vm.tryToLogin() },
        onRegisterButtonClick = { vm.tryToRegister() },
    )
}

@Composable
private fun LoginScreenContent(
    uiState: State,
    onNameChange: (String) -> Unit,
    onLoginButtonClick: () -> Unit,
    onRegisterButtonClick: () -> Unit,
) {
    Surface {
        SnackBarContainer(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.systemBars)
                .fillMaxSize(),
            snackBarState = uiState.snackBarState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                InfiniteLottieAnimation(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .aspectRatio(1f),
                    animationId = R.raw.chatter
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp),
                    value = uiState.name,
                    onValueChange = { onNameChange(it) },
                    singleLine = true,
                    label = {
                        Text(
                            text = stringResource(id = R.string.login_screen_name_title),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    isError = uiState.validationError,
                    supportingText = {
                        if (uiState.validationError) {
                            Text(
                                text = stringResource(id = R.string.login_screen_name_hint),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    },
                    trailingIcon = {
                        if (uiState.name.isNotBlank()) {
                            IconButton(onClick = { onNameChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null,
                                )
                            }
                        }
                    },
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp, horizontal = 48.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    LoadingButton(
                        modifier = Modifier.weight(1f),
                        title = stringResource(id = R.string.login_screen_login_button_title),
                        isLoading = uiState.isLoginButtonLoading,
                        onClick = onLoginButtonClick
                    )

                    LoadingButton(
                        modifier = Modifier.weight(1f),
                        title = stringResource(id = R.string.login_screen_register_button_title),
                        isLoading = uiState.isRegisterButtonLoading,
                        onClick = onRegisterButtonClick
                    )
                }
            }
        }
    }
}

@UIModePreviews
@Composable
private fun LoginScreenPreview() {
    ChatterTheme {
        LoginScreenContent(
            uiState = State(),
            onNameChange = {},
            onLoginButtonClick = {},
            onRegisterButtonClick = {},
        )
    }
}
