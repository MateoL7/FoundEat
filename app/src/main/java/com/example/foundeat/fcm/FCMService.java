package com.example.foundeat.fcm;

import android.util.Log;

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

        Log.e(">>>>>From",remoteMessage.getFrom());

        if(remoteMessage.getFrom().equals("/topics/news")){
            NotificationUtil.showNotification(
                    getApplicationContext(),
                    "Nuevo Restaurante",
                    "¡Conoce ya lo que este nuevo restaurante tiene para ofrecerte!");
        }else{
            NotificationUtil.showNotification(
                    getApplicationContext(),
                    "Nueva Reseña",
                    "¡Un cliente te ha dejado una reseña!");
        }



    }
}
