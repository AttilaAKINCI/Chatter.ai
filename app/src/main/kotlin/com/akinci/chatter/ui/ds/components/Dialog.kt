package com.akinci.chatter.ui.ds.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.akinci.chatter.core.compose.UIModePreviews
import com.akinci.chatter.ui.ds.theme.ChatterTheme
import com.akinci.chatter.ui.ds.theme.bodyLargeBold

@Composable
fun Dialog(
    title: String,
    description: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onConfirmButtonClick: () -> Unit,
    onDismissButtonClick: () -> Unit,
) {
    AlertDialog(
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        ),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                shape = MaterialTheme.shapes.extraSmall,
                onClick = onConfirmButtonClick
            ) {
                Text(
                    text = confirmButtonText,
                    style = MaterialTheme.typography.bodyLargeBold,
                )
            }
        },
        dismissButton = {
            Button(
                shape = MaterialTheme.shapes.extraSmall,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                onClick = onDismissButtonClick
            ) {
                Text(
                    text = dismissButtonText,
                    style = MaterialTheme.typography.bodyLargeBold,
                )
            }
        },
        onDismissRequest = { },
    )
}

@UIModePreviews
@Composable
private fun DialogPreview() {
    ChatterTheme {
        Surface {
            Dialog(
                title = "Confirmation required",
                description = "Do you really want to logout ? \n\nIf you want to stay and continue to chat with people more, you can press \"Close\" to dismiss dialog.",
                confirmButtonText = "Logout",
                dismissButtonText = "Close",
                onConfirmButtonClick = {},
                onDismissButtonClick = {},
            )
        }
    }
}