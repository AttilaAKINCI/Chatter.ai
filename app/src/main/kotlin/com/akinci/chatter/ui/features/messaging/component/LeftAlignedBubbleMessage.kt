package com.akinci.chatter.ui.features.messaging.component

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.akinci.chatter.R
import com.akinci.chatter.core.compose.UIModePreviews
import com.akinci.chatter.ui.ds.components.TiledBackground
import com.akinci.chatter.ui.ds.theme.ChatterTheme

// TODO Can we merge this AlignedBubbleMessages ?

@Composable
fun ColumnScope.LeftAlignedBubbleMessage(
    modifier: Modifier = Modifier,
    text: String,
    time: String,
) {
    Row {
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = modifier
                .shadow(
                    elevation = 4.dp,
                    shape = MaterialTheme.shapes.extraSmall,
                )
                .clip(MaterialTheme.shapes.extraSmall)
                .background(color = MaterialTheme.colorScheme.surfaceVariant),
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.fillMaxWidth(0.2f))
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@UIModePreviews
@Composable
private fun LeftAlignedBubbleMessagePreview() {
    ChatterTheme {
        Surface {
            TiledBackground(patternId = R.drawable.ic_pattern_bg) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    LeftAlignedBubbleMessage(
                        text = "Hello, I am Attila :) How are you ?",
                        time = "21:00"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LeftAlignedBubbleMessage(
                        text = "Hello, I am Attila :) How are you ?",
                        time = ""
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}