package com.fiap.antiRoubo.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import com.fiap.antiRoubo.model.NavItem

class NavItemRepository {
    fun getNavItems(): List<NavItem> {
        return listOf(
            NavItem(
                label = "Home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                iconDescription = "Home icon"
            ),
            NavItem(
                label = "Settings",
                selectedIcon = Icons.Filled.Settings,
                unselectedIcon = Icons.Outlined.Settings,
                iconDescription = "Settings icon"
            )
        )
    }
}