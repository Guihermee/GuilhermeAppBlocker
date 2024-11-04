package com.fiap.antiRoubo

import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fiap.antiRoubo.receivers.MyDeviceAdminReceiver
import com.fiap.antiRoubo.screens.MainScreen
import com.fiap.antiRoubo.services.MyAccessibilityService
import com.fiap.antiRoubo.ui.theme.AntiRouboTheme
import com.fiap.antiRoubo.viewModel.MainViewModel
import kotlinx.serialization.Serializable


class MainActivity : ComponentActivity() {

    companion object {
        lateinit var instance: MainActivity
    }

    // Device Admin permission request
    private val deviceAdminResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _: ActivityResult ->
            checkRequestDeviceAdmin()
        }

    fun checkRequestDeviceAdmin() {
        val componentName = ComponentName(this, MyDeviceAdminReceiver::class.java)
        if (!isDeviceAdminEnabled()) {
            requestService(
                title = getString(R.string.sample_device_admin_title),
                message = getString(R.string.sample_device_admin_description),
                intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                    putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
                    putExtra(
                        DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        getString(R.string.sample_device_admin_description)
                    )
                },
                resultLauncher = deviceAdminResultLauncher
            )
        }
    }

    fun isDeviceAdminEnabled(): Boolean {
        val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val componentName = ComponentName(this, MyDeviceAdminReceiver::class.java)
        return dpm.isAdminActive(componentName)
    }

    // Accessibility Service
    private val accessibilityServiceResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _: ActivityResult ->
            checkRequestAccessibilityService()
        }

    fun checkRequestAccessibilityService() {
        if (!isAccessibilityServiceEnabled(this)) {
            requestService(
                title = getString(R.string.accessibility_service_title),
                message = getString(R.string.enable_service_dialog_desc),
                intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS),
                resultLauncher = accessibilityServiceResultLauncher
            )
        }
    }

    fun isAccessibilityServiceEnabled(context: Context): Boolean {
        val accessibilityManager =
            context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        return accessibilityManager.isEnabled
    }

    // Others
    private fun requestService(
        title: String,
        message: String,
        intent: Intent,
        resultLauncher: ActivityResultLauncher<Intent>
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { _, _ ->
                run {
                    resultLauncher.launch(intent)
                }
            }
            .create().show()
    }

    fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            instance = this

            AntiRouboTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = HomePage) {
                        composable<HomePage> {
                            MainScreen(MainViewModel())
                        }
                    }
                }
            }
        }
    }
}

@Serializable
object HomePage

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}