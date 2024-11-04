package com.fiap.antiRoubo.myComponents

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fiap.antiRoubo.model.MyService
import com.fiap.antiRoubo.viewModel.HomeViewModel

@Composable
fun MyServiceView(
    myService: MyService,
    isExpanded: Boolean,
    homeViewModel: HomeViewModel,
    index: Int,
    selectedItem: Int?
) {

    val TAG = "MyServiceView"

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                Log.i(TAG, "MyServiceView: Button Clicked")
                if (selectedItem != null && selectedItem == index) {
                    homeViewModel.setSelectedService(null)
                } else {
                    homeViewModel.setSelectedService(index)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = MaterialTheme.colorScheme.primary)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Icon And Text
                Row {
                    Icon(
                        imageVector = myService.icon,
                        contentDescription = myService.iconDescription
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Accessibility Service")
                }

                // Status
                Icon(
                    imageVector = Icons.Default.Circle,
                    contentDescription = "Circle Icon",
                    tint = if (myService.enabled) Color.Green else Color.Red
                )
            }
        }

        // Description and Buttons
        if (isExpanded) {
            // Description
            Text(
                text = myService.description,
                modifier = Modifier.padding(horizontal = 36.dp)
            )

            // Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextButton(onClick = myService.onEnabled) {
                    Text(text = "Enable")
                }
                TextButton(onClick = myService.onDisabled) {
                    Text(text = "Disable")
                }
            }
        }

    }
}