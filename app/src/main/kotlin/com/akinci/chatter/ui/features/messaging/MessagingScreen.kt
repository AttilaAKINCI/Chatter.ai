package com.akinci.chatter.ui.features.messaging

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.akinci.chatter.R
import com.akinci.chatter.core.compose.UIModePreviews
import com.akinci.chatter.domain.data.ChatSession
import com.akinci.chatter.domain.data.MessageItem
import com.akinci.chatter.domain.data.User
import com.akinci.chatter.ui.ds.components.BubbleMessage
import com.akinci.chatter.ui.ds.components.CachedImage
import com.akinci.chatter.ui.ds.components.IconButton
import com.akinci.chatter.ui.ds.components.TiledBackground
import com.akinci.chatter.ui.ds.theme.ChatterTheme
import com.akinci.chatter.ui.ds.theme.oval
import com.akinci.chatter.ui.features.messaging.MessagingViewContract.ScreenArgs
import com.akinci.chatter.ui.features.messaging.MessagingViewContract.State
import com.akinci.chatter.ui.navigation.animation.SlideHorizontallyAnimation
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.PersistentList
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
        onTextChanged = { vm.onTextChanged(it) },
        onSendButtonClick = { vm.onSendButtonClick() },
        onBackClick = { navigator.navigateUp() },
    )
}

@Composable
private fun MessagingScreenContent(
    uiState: State,
    onTextChanged: (String) -> Unit,
    onSendButtonClick: () -> Unit,
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

                MessagingScreen.Messages(
                    modifier = Modifier.weight(1f),
                    messages = uiState.messages,
                )

                MessagingScreen.Footer(
                    text = uiState.text,
                    onTextChanged = onTextChanged,
                    onSendButtonClick = onSendButtonClick,
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
            IconButton(
                modifier = Modifier.padding(horizontal = 8.dp),
                painter = rememberVectorPainter(image = Icons.AutoMirrored.Rounded.ArrowBack),
                onClick = onBackClick
            )
        }
    )
    HorizontalDivider()
}

@Composable
private fun MessagingScreen.Footer(
    text: String,
    onTextChanged: (String) -> Unit,
    onSendButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        HorizontalDivider()
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
                value = text,
                textStyle = MaterialTheme.typography.bodyMedium,
                trailingIcon = {
                    AnimatedVisibility(
                        visible = text.isNotBlank(),
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut(),
                    ) {
                        IconButton(
                            painter = rememberVectorPainter(image = Icons.Default.Cancel),
                            size = 24.dp,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            tintColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            onClick = { onTextChanged("") }
                        )
                    }
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.messaging_screen_text_placeholder),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                onValueChange = onTextChanged,
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                painter = painterResource(id = R.drawable.ic_send),
                size = 56.dp,
                containerColor = MaterialTheme.colorScheme.primary,
                tintColor = MaterialTheme.colorScheme.onPrimary,
                onClick = onSendButtonClick
            )
        }
    }
}

@Composable
private fun MessagingScreen.Messages(
    modifier: Modifier = Modifier,
    messages: PersistentList<MessageItem>,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.weight(1f))
        LazyColumn(
            state = rememberLazyListState(),
            reverseLayout = true,
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(messages) { messageItem ->
                BubbleMessage(messageItem = messageItem)
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
                        name = "Jessica Alba",
                        userName = "attilaakinci",
                        imageUrl = "",
                        phone = "+3164637829",
                        nationality = "TR",
                    )
                ),
                messages = persistentListOf(
                    MessageItem.TypeIndicatorItem,
                    MessageItem.OutboundMessageItem(
                        text = "Hello, I am Attila :) How are you ?",
                        time = "21:00"
                    ),
                    MessageItem.InboundMessageItem(
                        text = "Hi there !! I am just fine. What about you ?",
                        time = "21:01"
                    ),
                ),
            ),
            onTextChanged = {},
            onSendButtonClick = {},
            onBackClick = {},
        )
    }
}
