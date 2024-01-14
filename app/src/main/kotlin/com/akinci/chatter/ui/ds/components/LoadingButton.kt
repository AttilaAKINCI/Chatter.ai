package com.akinci.chatter.ui.ds.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.akinci.chatter.core.compose.UIModePreviews
import com.akinci.chatter.ui.ds.theme.ChatterTheme
import com.akinci.chatter.ui.ds.theme.bodyLargeBold

// TODO kdoc
@Composable
fun LoadingButton(
    modifier: Modifier = Modifier,
    title: String,
    isLoading: Boolean,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        onClick = onClick
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 3.dp,
                )
            }

            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .alpha(if (isLoading) 0f else 1f),
                text = title,
                style = MaterialTheme.typography.bodyLargeBold,
            )
        }
    }
}

@Composable
@UIModePreviews
private fun LoadingButtonPreview() {
    ChatterTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LoadingButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = "Login",
                isLoading = false,
                onClick = {},
            )

            LoadingButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = "Login",
                isLoading = true,
                onClick = {},
            )
        }
    }
}
