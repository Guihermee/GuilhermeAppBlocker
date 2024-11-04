package com.fiap.antiRoubo.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.fiap.antiRoubo.repository.SharedPrefs
import com.fiap.antiRoubo.services.MyAccessibilityService

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {

            val serviceIntent = Intent(context, MyAccessibilityService::class.java)
            context?.startService(serviceIntent)

            if (context != null) {
                val sharedPrefs = SharedPrefs(context)
                if (sharedPrefs.getBlockPhoneState()) {
                    MyAccessibilityService.instance.blockPhoneOfTheUser(
                        MyAccessibilityService.instance.timer,
                        context
                    )
                }
            }
        }
    }
}