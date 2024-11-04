package com.fiap.antiRoubo.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.fiap.antiRoubo.repository.NavItemRepository
import com.fiap.antiRoubo.viewModel.HomeViewModel
import com.fiap.antiRoubo.viewModel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainViewModel: MainViewModel
) {

    val navItemList = NavItemRepository().getNavItems()
    val selectedItemList by mainViewModel.selectedItemList.observeAsState(initial = 0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Guilherme")
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedItemList == index,
                        onClick = { mainViewModel.setSelectedItemList(index) },
                        icon = {
                            Icon(
                                imageVector = if (selectedItemList == index) navItem.selectedIcon else navItem.unselectedIcon,
                                contentDescription = navItem.iconDescription
                            )
                        },
                        label = { Text(text = navItem.label) }
                    )
                }
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            when (selectedItemList) {
                0 -> {
                    HomeScreen(HomeViewModel())
                }

                1 -> {
                    SettingsScreen()
                }
            }
        }
    }
}