package com.example.foundeat.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.foundeat.R;
import com.example.foundeat.ui.client.ClientHome;

public class NotificationUtil {


    private static final String CHANNEL_ID = "messages";
    private static final String CHANNEL_NAME = "Messages";

    private static int id = 0;

    public static void showNotification(Context context, String title, String message) {

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(context, ClientHome.class);
        PendingIntent pendingintent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.notification)
                .setContentIntent(pendingintent);

        Notification notification = builder.build();
        manager.notify(id, notification);
        id++;
    }
}
