package com.fiap.antiRoubo.receivers;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fiap.antiRoubo.R;

import java.util.Timer;
import java.util.TimerTask;

public class MyDeviceAdminReceiver extends DeviceAdminReceiver {
    public static MyDeviceAdminReceiver instance;
    private static final String TAG = "MyDeviceAdminReceiver";
    DevicePolicyManager dpm;
    long current_time;
    Timer myThread;

    @Override
    public void onEnabled(@NonNull Context context, @NonNull Intent intent) {
        super.onEnabled(context, intent);
        Log.w(TAG, context.getString(R.string.device_admin_ativado_com_sucesso));
        instance = this;
        dpm = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
    }

    @Override
    public void onDisabled(@NonNull Context context, @NonNull Intent intent) {
        super.onDisabled(context, intent);
        Log.w(TAG, "onDisabled: Serviço de administrador desativado!");
    }

    @Override
    public void onPasswordFailed(@NonNull Context context, @NonNull Intent intent) {
        super.onPasswordFailed(context, intent);
        Log.i(TAG, "Senha foi digitada incorretamente!");

        myThread = new Timer();
        current_time = System.currentTimeMillis();
//        myThread.schedule(lock_task, 0, 1000);
    }

    public void disableDeviceAdmin(Context context) {
        ComponentName devAdminReceiver = new ComponentName(context, MyDeviceAdminReceiver.class);
        DevicePolicyManager dpm = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        dpm.removeActiveAdmin(devAdminReceiver);
    }

    public TimerTask lock_task = new TimerTask() {
        @Override
        public void run() {
            long diff = System.currentTimeMillis() - current_time;
            if (diff < 5000) {
                Log.d("Timer", "1 second");
                dpm.lockNow();
            } else {
                myThread.cancel();
            }
        }
    };
    
}
