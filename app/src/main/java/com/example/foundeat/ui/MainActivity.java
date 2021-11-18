package com.example.foundeat.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private Button clientBtn, restaurantBtn;
    private Client client;
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Cargar el usuario o restaurante
        client = loadClient();
        restaurant = loadRestaurant();
        if((client == null && restaurant == null) || FirebaseAuth.getInstance().getCurrentUser() == null || !FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){

        }else{
            if(client!= null){
                Intent intent = new Intent(this,ClientHome.class);
                intent.putExtra("client",client);
                startActivity(intent);
            }else if(restaurant!=null){
                Intent intent = new Intent(this,RestaurantHome.class);
                intent.putExtra("restaurant",restaurant);
                startActivity(intent);
            }
        }

        clientBtn = findViewById(R.id.loginBtn);
        restaurantBtn = findViewById(R.id.registerBtn);

        clientBtn.setOnClickListener(v->{
            Intent intent = new Intent(this,ClientScreen.class);
            startActivity(intent);
        });
        restaurantBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this,RestaurantScreen.class);
            startActivity(intent);
        });

    }

    private Client loadClient(){
        String json = getSharedPreferences("foundEat", MODE_PRIVATE).getString("client", "NO_USER");
        if(json.equals("NO_USER")){
            return null;
        }else{
            return new Gson().fromJson(json, Client.class);
        }
    }
    private Restaurant loadRestaurant(){
        String json = getSharedPreferences("foundEat", MODE_PRIVATE).getString("restaurant", "NO_USER");
        if(json.equals("NO_USER")){
            return null;
        }else{
            return new Gson().fromJson(json, Restaurant.class);
        }
    }


}