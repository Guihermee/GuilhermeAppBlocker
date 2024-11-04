package com.fiap.antiRoubo.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.fiap.antiRoubo.MainActivity
import com.fiap.antiRoubo.myComponents.MyServiceView
import com.fiap.antiRoubo.repository.MyServiceRepository
import com.fiap.antiRoubo.services.MyAccessibilityService
import com.fiap.antiRoubo.viewModel.HomeViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel
) {
    val context = LocalContext.current
    val selectedService by homeViewModel.selectedService.observeAsState(initial = null)
    val myServices by homeViewModel.myServices.observeAsState(initial = MyServiceRepository().getAllServices(context, homeViewModel))

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}
            Lifecycle.State.RESUMED -> {
                homeViewModel.setMyServices(MyServiceRepository().getAllServices(context, homeViewModel))
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Services Status", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            myServices.forEachIndexed { index, myService ->  
                MyServiceView(
                    myService = myService,
                    isExpanded = index == selectedService,
                    homeViewModel = homeViewModel,
                    index = index,
                    selectedItem = selectedService
                )
            }
        }

    }
}