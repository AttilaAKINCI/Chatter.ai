package com.akinci.chatter.ui.ds.components.snackbar

import androidx.annotation.StringRes
import java.util.UUID

data class SnackBarState(
    val id: UUID = UUID.randomUUID(),
    @StringRes val messageId: Int,
)