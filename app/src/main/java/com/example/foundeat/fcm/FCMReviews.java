package com.example.foundeat.fcm;

import androidx.annotation.NonNull;

import com.example.foundeat.util.NotificationUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class FCMReviews extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        NotificationUtil.showNotification(
                getApplicationContext(),
                "Nueva Reseña",
                "Un usuario acaba de dejarte una nueva reseña");

    }
}