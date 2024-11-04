package com.fiap.antiRoubo.model

import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconDescription: String
)