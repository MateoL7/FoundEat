package com.example.foundeat.fcm;

import androidx.annotation.NonNull;

import com.example.foundeat.util.NotificationUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONObject;

public class FCMService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String msg = remoteMessage.getData().toString();
        NotificationUtil.showNotification(getApplicationContext(),"Nuevo mensaje",msg);

    }
}
