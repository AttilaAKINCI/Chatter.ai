package com.akinci.chatter.ui.ds.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.akinci.chatter.R
import com.akinci.chatter.core.compose.UIModePreviews
import com.akinci.chatter.domain.data.MessageItem
import com.akinci.chatter.ui.ds.theme.ChatterTheme

/**
 *  [BubbleMessage] is message display component with 2 different type namely [Content], [Indicator]
 *  messages can be left/right aligned regarding type of [MessageItem]
 *
 *  @property [modifier] compose modifier
 *  @property [messageItem] data model
 *
 * **/
@Composable
fun BubbleMessage(
    modifier: Modifier = Modifier,
    messageItem: MessageItem,
) {
    when (messageItem) {
        is MessageItem.OutboundMessageItem ->
            RightAlignedContainer(
                modifier = modifier,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            ) {
                Content(
                    text = messageItem.text,
                    time = messageItem.time,
                )
            }

        is MessageItem.InboundMessageItem ->
            LeftAlignedContainer(
                modifier = modifier,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ) {
                Content(
                    text = messageItem.text,
                    time = messageItem.time,
                )
            }

        is MessageItem.TypeIndicatorItem ->
            LeftAlignedContainer(
                modifier = modifier,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ) {
                Indicator()
            }
    }
}

@Composable
private fun ColumnScope.Content(
    text: String,
    time: String,
) {
    Text(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    if (time.isNotBlank()) {
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.End),
            text = time,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun Indicator() {
    InfiniteLottieAnimation(
        modifier = Modifier
            .scale(3f)
            .padding(start = 15.dp, end = 16.dp),
        animationId = R.raw.type_indicator
    )
}

@Composable
private fun RightAlignedContainer(
    modifier: Modifier = Modifier,
    containerColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Row(modifier = modifier) {
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.fillMaxWidth(0.2f))
        Column(
            modifier = Modifier
                .shadow(
                    elevation = 4.dp,
                    shape = MaterialTheme.shapes.extraSmall,
                )
                .clip(MaterialTheme.shapes.extraSmall)
                .background(color = containerColor),
        ) {
            content()
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
private fun LeftAlignedContainer(
    modifier: Modifier = Modifier,
    containerColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Row(modifier = modifier) {
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .shadow(
                    elevation = 4.dp,
                    shape = MaterialTheme.shapes.extraSmall,
                )
                .clip(MaterialTheme.shapes.extraSmall)
                .background(color = containerColor),
        ) {
            content()
        }
        Spacer(modifier = Modifier.fillMaxWidth(0.2f))
        Spacer(modifier = Modifier.weight(1f))
    }
}

@UIModePreviews
@Composable
private fun BubbleMessagePreview() {
    ChatterTheme {
        Surface {
            TiledBackground(patternId = R.drawable.ic_pattern_bg) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    BubbleMessage(
                        modifier = Modifier,
                        messageItem = MessageItem.TypeIndicatorItem,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    BubbleMessage(
                        modifier = Modifier,
                        messageItem = MessageItem.OutboundMessageItem(
                            text = "Hello, I am Attila :) How are you ?",
                            time = "21:00"
                        ),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    BubbleMessage(
                        modifier = Modifier,
                        messageItem = MessageItem.InboundMessageItem(
                            text = "Hi there !! I am just fine. What about you ?",
                            time = "21:01"
                        ),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}