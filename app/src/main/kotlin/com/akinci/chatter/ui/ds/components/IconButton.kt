package com.akinci.chatter.ui.ds.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.akinci.chatter.core.compose.UIModePreviews
import com.akinci.chatter.ui.ds.theme.ChatterTheme
import com.akinci.chatter.ui.ds.theme.oval
import androidx.compose.material3.IconButton as MaterialIconButton

/**
 *  IconButton is override button component of [MaterialIconButton]
 *
 *  @property [modifier] compose modifier
 *  @property [painter] icon resource
 *  @property [size] width and height of icon
 *  @property [containerColor] background color of icon
 *  @property [tintColor] icon resource tint color
 *  @property [onClick] handler for click actions
 *
 * **/
@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    size: Dp = 40.dp,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    tintColor: Color = LocalContentColor.current,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(MaterialTheme.shapes.oval)
            .background(color = containerColor)
            .clickable(
                indication = rememberRipple(),
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.size(size * 0.8f),
            painter = painter,
            contentDescription = null,
            tint = tintColor,
        )
    }
}

@UIModePreviews
@Composable
private fun IconButtonPreview() {
    ChatterTheme {
        Column {
            Separator()
            IconButton(
                painter = rememberVectorPainter(image = Icons.Default.Clear),
                containerColor = MaterialTheme.colorScheme.primary,
                tintColor = MaterialTheme.colorScheme.onPrimary,
                onClick = {},
            )
            Separator()
            IconButton(
                size = 56.dp,
                painter = rememberVectorPainter(image = Icons.Default.Clear),
                containerColor = MaterialTheme.colorScheme.primary,
                tintColor = MaterialTheme.colorScheme.onPrimary,
                onClick = {},
            )
        }
    }
}

@Composable
private fun Separator() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
            .background(color = Color.Red)
    )
}
