package com.example.foundeat.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;

public class MainActivity extends AppCompatActivity {

    private Button clientBtn, restaurantBtn;
    private Client client;
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Cargar el usuario o restaurante
        

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


}