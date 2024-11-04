package com.fiap.antiRoubo.repository

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.AdminPanelSettings
import com.fiap.antiRoubo.MainActivity
import com.fiap.antiRoubo.model.MyService
import com.fiap.antiRoubo.receivers.MyDeviceAdminReceiver
import com.fiap.antiRoubo.services.MyAccessibilityService
import com.fiap.antiRoubo.viewModel.HomeViewModel

class MyServiceRepository {
    fun getAllServices(context: Context, homeViewModel: HomeViewModel): List<MyService> {
        return listOf(
            MyService(
                name = "Accessibility Service",
                description = "We use Acessibility Service to retrive information about Keys of your phone has, like Volume up or down to interpret it as Morse Code!",
                icon = Icons.Default.Accessibility,
                iconDescription = "Accessibility Icon",
                enabled = MainActivity.instance.isAccessibilityServiceEnabled(context),
                onDisabled = {
                    MyAccessibilityService.instance.disableSelf()
                    homeViewModel.setMyServices(
                        MyServiceRepository().getAllServices(
                            context,
                            homeViewModel
                        )
                    )
                    Toast.makeText(context, "Desativado", Toast.LENGTH_SHORT).show()
                },
                onEnabled = {
                    MainActivity.instance.checkRequestAccessibilityService()
                    homeViewModel.setMyServices(
                        MyServiceRepository().getAllServices(
                            context,
                            homeViewModel
                        )
                    )
                }
            ),
            MyService(
                name = "Admin Device",
                description = "We use admin device to block the phone to be used, this is useful if you manage to enable the function pre-built and configured by yourself before the phone before get robbed, hopefully you won't need it.",
                icon = Icons.Default.AdminPanelSettings,
                enabled = MainActivity.instance.isDeviceAdminEnabled(),
                iconDescription = "AdminPanelSettings Icon",
                onDisabled = {
//                    MyDeviceAdminReceiver.instance.disableDeviceAdmin(context)

                    val devAdminReceiver = ComponentName(context, MyDeviceAdminReceiver::class.java)
                    val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                    dpm.removeActiveAdmin(devAdminReceiver)

                    homeViewModel.setMyServices(
                        MyServiceRepository().getAllServices(context, homeViewModel)
                    )
                    Toast.makeText(context, "Desativado", Toast.LENGTH_SHORT).show()
                },
                onEnabled = {
                    MainActivity.instance.checkRequestDeviceAdmin()
                    homeViewModel.setMyServices(
                        MyServiceRepository().getAllServices(context, homeViewModel)
                    )
                }
            )
        )
    }
}