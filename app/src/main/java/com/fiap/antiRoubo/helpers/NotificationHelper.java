package com.fiap.antiRoubo.helpers;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.fiap.antiRoubo.MainActivity;
import com.fiap.antiRoubo.R;

public class NotificationHelper {
    private static final String TAG = "NotificationHelper";

    public void createNotification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_launcher_foreground, "Ativar novamente", pendingIntent);

        Notification builder = new NotificationCompat.Builder(context, context.getPackageName())
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.notification_description_acessibility))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .addAction(action)
                .setSmallIcon(R.drawable.baseline_accessibility_24)
                .build();

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = nm.getNotificationChannel(context.getPackageName());
            if (notificationChannel == null) {
                notificationChannel = new NotificationChannel(context.getPackageName(), context.getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(true);
                notificationChannel.enableVibration(true);
                nm.createNotificationChannel(notificationChannel);
            }
            Log.i(TAG, "onAccessibilityEvent: Notificação foi ativada!");
            nm.notify(1, builder);
        }
    }

}
