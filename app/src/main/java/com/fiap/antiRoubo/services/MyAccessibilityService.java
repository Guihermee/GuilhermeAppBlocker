package com.fiap.antiRoubo.services;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import androidx.core.app.NotificationCompat;

import com.fiap.antiRoubo.MainActivity;
import com.fiap.antiRoubo.R;
import com.fiap.antiRoubo.receivers.MyDeviceAdminReceiver;
import com.fiap.antiRoubo.repository.SharedPrefs;

import java.util.Timer;
import java.util.TimerTask;

public class MyAccessibilityService extends AccessibilityService {

    public static MyAccessibilityService instance;

    private static final String TAG = "MyAccessibilityService";
    private static final Long LongPressThreshold = 30L;
    private static Long VolumeDownPressedTime = 0L;
    private static Long TimeOfTheLastClick = 0L;
    private static String Code = "";

    boolean isActive = false;
    public Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Log.i("RecorderAccessibilityService", "executado! ");
            isActive = false;
        }
    };

    /**
     * Função segura que bloqueia o celular do usuário por 5s
     * @param timer
     * @param context
     */
    public void blockPhoneOfTheUser(Timer timer, Context context) {
        Long current_time = System.currentTimeMillis();
        DevicePolicyManager dpm = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);

        TimerTask lock_task = new TimerTask() {
            @Override
            public void run() {
                dpm.lockNow();
//                long diff = System.currentTimeMillis() - current_time;
//                if (diff < 50000) {
//                    Log.d("Timer", "1 second");
//
//                } else {
//                    timer.cancel();
//                }
            }
        };
        timer.schedule(lock_task, 0, 1000);
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {

        // Logica para ver se parou de digitar, pegar o texto e transformar em letra.
        if (isActive) {
            task.cancel();
            task = new TimerTask() {
                @Override
                public void run() {
                    Log.i(TAG, "onKeyEvent: Code: " + Code);
                    if (Code.equals(".-.") && MainActivity.instance.isDeviceAdminEnabled()) {
                        SharedPrefs sharedPrefs = new SharedPrefs(MainActivity.instance);
                        sharedPrefs.saveBlockPhoneState(true);
                        blockPhoneOfTheUser(timer, MainActivity.instance);
                    }
                    Code = "";
                    isActive = false;
                }
            };
            timer.schedule(task, 1000);
        } else {
            task = new TimerTask() {
                @Override
                public void run() {
                    Log.i(TAG, "onKeyEvent: Code: " + Code);
                    if (Code.equals(".-.") && MainActivity.instance.isDeviceAdminEnabled()) {
                        blockPhoneOfTheUser(timer, MainActivity.instance);
                    }
                    Code = "";
                    isActive = false;
                }
            };
            timer.schedule(task, 1000);
        }
        isActive = true;

        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
            VolumeDownPressedTime = System.currentTimeMillis();
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN && event.getAction() == KeyEvent.ACTION_UP) {
            long currentTime = System.currentTimeMillis();
            long timePressed = currentTime - VolumeDownPressedTime;

            long timeBetweenClicks;
            if (TimeOfTheLastClick != 0F) {
                timeBetweenClicks = currentTime - TimeOfTheLastClick;
            } else {
                timeBetweenClicks = 0;
            }

            if (timeBetweenClicks >= 500L && timeBetweenClicks < 1000L) {
                Log.i(TAG, "onKeyEvent: Code: " + Code);
                Code = "";
                Log.i(TAG, "onKeyEvent: Divisão de letra: ------------------------------------------------------");
            }

            if (timeBetweenClicks >= 1000L) {
                Code = "";
                Log.i(TAG, "onKeyEvent: Divisão de palavra; ===================================================");
            }

            if (timePressed < LongPressThreshold) {
                Log.i(TAG, "onKeyEvent: Short Click on Down volume, Time between the last click:  + " + timeBetweenClicks);
                Code = Code + ".";
                TimeOfTheLastClick = currentTime;
            } else {
                Log.i(TAG, "onKeyEvent: Long Click on Down volume, Time between the last click:  + " + timeBetweenClicks);
                Code = Code + "-";
                TimeOfTheLastClick = currentTime;
            }
        }

        return super.onKeyEvent(event);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        try {
            if (event.getPackageName().toString().contains("com.nu.production")) {

                Intent intent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_launcher_foreground, "Ativar novamente", pendingIntent);

                Notification builder = new NotificationCompat.Builder(this, this.getPackageName())
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.notification_description_acessibility))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .addAction(action)
                        .setSmallIcon(R.drawable.baseline_accessibility_24)
                        .build();

                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = nm.getNotificationChannel(this.getPackageName());
                    if (notificationChannel == null) {
                        notificationChannel = new NotificationChannel(this.getPackageName(), getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                        notificationChannel.enableLights(true);
                        notificationChannel.enableVibration(true);
                        nm.createNotificationChannel(notificationChannel);
                    }
                    Log.i(TAG, "onAccessibilityEvent: Notificação foi ativada!");
                    nm.notify(1, builder);
                }

                Log.w(TAG, "onAccessibilityEvent: Accessibility Service was disabled successfully!");
                disableSelf();
            }

        } catch (Exception e) {
            Log.e(TAG, "onAccessibilityEvent: Algo deu errado!" + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: Serviço de acessibilidade terminou!");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onAccessibilityEvent: Serviço de Acessibilidade iniciou!");
        instance = this;
    }

    @Override
    public void onInterrupt() {

    }
}
