package com.fiap.antiRoubo.myComponents

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = if (isPermanentlyDeclined) onGoToAppSettingsClick else onDismiss,
        confirmButton = {
            Button(onClick = onOkClick) {
                Text(text = if (isPermanentlyDeclined) "Grant permission" else "Ok")
            }
        },
        title = { Text(text = "Permission Required") },
        text = { Text(text = permissionTextProvider.getDescription(isPermanentlyDeclined)) }
        )

}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class RecordAudioPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "It seems you permanently declined microphone permission. " +
                    "You can go to the app settings to grant it."
        } else {
            "This app needs access to your microphone so that your friends " +
                    "can hear you in a call."
        }
    }
}

