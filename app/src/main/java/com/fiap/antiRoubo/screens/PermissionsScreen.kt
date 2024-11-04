package com.fiap.antiRoubo.screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.fiap.antiRoubo.MainActivity
import com.fiap.antiRoubo.myComponents.PermissionDialog
import com.fiap.antiRoubo.myComponents.RecordAudioPermissionTextProvider
import com.fiap.antiRoubo.viewModel.PermissionsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionsScreen(
    permissionsViewModel: PermissionsViewModel
) {
    val dialogQueue = permissionsViewModel.visiblePermissionDialogQueue
    val permissionsToRequest = arrayOf(
        android.Manifest.permission.RECORD_AUDIO
    )

    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            permissionsToRequest.forEach { permission ->
                permissionsViewModel.onPermissionResult(
                    permission = permission,
                    isGranted = perms[permission] == true
                )
            }
        }
    )

    dialogQueue
        .reversed()
        .forEach { permission ->
            PermissionDialog(
                permissionTextProvider = when (permission) {
                    android.Manifest.permission.RECORD_AUDIO -> {
                        RecordAudioPermissionTextProvider()
                    }

                    else -> return@forEach
                },
                isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                    MainActivity(),
                    permission
                ),
                onDismiss = permissionsViewModel::dismissDialog,
                onOkClick = {
                    permissionsViewModel.dismissDialog()
                    multiplePermissionResultLauncher.launch(
                        arrayOf(permission)
                    )
                },
                onGoToAppSettingsClick = {}//::openAppSettings
            )
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Permissions")
                }
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
            }
        }
    }
}

