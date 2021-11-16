package com.example.foundeat.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.foundeat.R;

public class MainActivity extends AppCompatActivity {

    private Button clientBtn, restaurantBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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