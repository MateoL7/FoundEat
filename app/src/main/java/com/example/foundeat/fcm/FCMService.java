package com.example.foundeat.fcm;

import androidx.annotation.NonNull;

import com.example.foundeat.model.Restaurant;
import com.example.foundeat.util.NotificationUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONObject;

public class FCMService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //String msg = remoteMessage.getData().toString();
        JSONObject object = new JSONObject(remoteMessage.getData());
        String json = object.toString();
        Restaurant r = new Gson().fromJson(json, Restaurant.class);

        NotificationUtil.showNotification(
                getApplicationContext(),
                "Nuevo Restaurante",
                "¡Conoce ya lo que "+ r.getName()+" tiene para ofrecerte!");

    }
}
