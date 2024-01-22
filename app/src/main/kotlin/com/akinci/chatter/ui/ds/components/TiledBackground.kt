package com.akinci.chatter.ui.ds.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap

/**
 *  TiledBackground is a wrapper box layout which add tiled background image behind the content
 *
 *  @property [modifier] compose modifier
 *  @property [patternId] tiled background image resource id
 *  @property [content] composable content
 *
 * **/
@Composable
fun TiledBackground(
    modifier: Modifier = Modifier,
    @DrawableRes patternId: Int,
    content: @Composable BoxScope.() -> Unit
) {
    val context = LocalContext.current
    val image = context.getDrawable(patternId)
        ?.toBitmap()
        ?.asImageBitmap()

    val brush = remember(image) {
        ShaderBrush(
            ImageShader(image!!, TileMode.Repeated, TileMode.Repeated)
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush)
    ) {
        content()
    }
}
