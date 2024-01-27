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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.akinci.chatter.R
import com.akinci.chatter.core.compose.UIModePreviews
import com.akinci.chatter.ui.ds.components.InfiniteLottieAnimation
import com.akinci.chatter.ui.ds.components.TiledBackground
import com.akinci.chatter.ui.ds.theme.ChatterTheme

@Composable
fun ColumnScope.TypeIndicatorBubbleMessage(
    modifier: Modifier = Modifier,
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
            InfiniteLottieAnimation(
                modifier = Modifier
                    .scale(3f)
                    .padding(start = 15.dp, end = 16.dp),
                animationId = R.raw.type_indicator
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@UIModePreviews
@Composable
private fun TypeIndicatorBubbleMessagePreview() {
    ChatterTheme {
        Surface {
            TiledBackground(patternId = R.drawable.ic_pattern_bg) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    TypeIndicatorBubbleMessage()
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}