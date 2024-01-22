package com.akinci.chatter.ui.features.messaging

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.akinci.chatter.R
import com.akinci.chatter.core.compose.UIModePreviews
import com.akinci.chatter.domain.chatwindow.ChatSession
import com.akinci.chatter.domain.user.User
import com.akinci.chatter.ui.ds.components.CachedImage
import com.akinci.chatter.ui.ds.components.TiledBackground
import com.akinci.chatter.ui.ds.theme.ChatterTheme
import com.akinci.chatter.ui.ds.theme.oval
import com.akinci.chatter.ui.features.messaging.MessagingViewContract.ScreenArgs
import com.akinci.chatter.ui.features.messaging.MessagingViewContract.State
import com.akinci.chatter.ui.navigation.animation.SlideHorizontallyAnimation
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.persistentListOf

@Composable
@Destination(
    style = SlideHorizontallyAnimation::class,
    navArgsDelegate = ScreenArgs::class,
)
fun MessagingScreen(
    navigator: DestinationsNavigator,
    vm: MessagingViewModel = hiltViewModel(),
) {
    val uiState: State by vm.stateFlow.collectAsStateWithLifecycle()

    MessagingScreenContent(
        uiState = uiState,
        onBackClick = { navigator.navigateUp() },
    )
}

@Composable
private fun MessagingScreenContent(
    uiState: State,
    onBackClick: () -> Unit,
) {
    Surface {
        TiledBackground(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.navigationBars)
                .fillMaxSize(),
            patternId = R.drawable.ic_pattern_bg
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                MessagingScreen.TopBar(
                    name = uiState.session.chatMate.name,
                    imageUrl = uiState.session.chatMate.imageUrl,
                    onBackClick = onBackClick,
                )

                // messaging area
                Column(modifier = Modifier.weight(1f)) {

                }

                MessagingScreen.Footer(
                    onSendButtonClick = {},
                )
            }
        }
    }
}

typealias MessagingScreen = Unit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MessagingScreen.TopBar(
    name: String,
    imageUrl: String,
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CachedImage(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .size(48.dp)
                        .clip(MaterialTheme.shapes.oval),
                    imageUrl = imageUrl,
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
    Divider()
}

@Composable
private fun MessagingScreen.Footer(
    onSendButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Divider()
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
                    .clip(MaterialTheme.shapes.oval),
                colors = TextFieldDefaults.colors(
                    disabledIndicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                    errorIndicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                singleLine = true,
                value = "asd",
                trailingIcon = {
                    // TODO add clear icon.
                },
                onValueChange = {},
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onSendButtonClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = null,
                )
            }
        }
    }
}


@UIModePreviews
@Composable
private fun MessagingScreenPreview() {
    ChatterTheme {
        MessagingScreenContent(
            uiState = State(
                session = ChatSession(
                    sessionId = 100L,
                    chatMate = User(
                        id = 1,
                        name = "Attila Akinci",
                        userName = "attilaakinci",
                        imageUrl = "",
                        phone = "+3164637829",
                        nationality = "TR",
                    )
                ),
                messages = persistentListOf(),
            ),
            onBackClick = {},
        )
    }
}
