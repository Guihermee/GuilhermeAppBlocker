package com.fiap.antiRoubo.model

import androidx.compose.ui.graphics.vector.ImageVector

data class MyService(
    val name: String,
    val description: String,
    val icon: ImageVector,
    val iconDescription: String,
    val enabled: Boolean,
    val onDisabled: () -> Unit,
    val onEnabled: () -> Unit,
)