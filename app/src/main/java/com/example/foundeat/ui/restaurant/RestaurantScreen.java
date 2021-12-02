package com.example.foundeat.ui.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.foundeat.R;
import com.example.foundeat.ui.Login;
import com.example.foundeat.ui.MainActivity;

public class RestaurantScreen extends AppCompatActivity {

    private Button loginBtn, registerBtn;
    private ImageButton backBtnRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_screen);

        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        backBtnRes = findViewById(R.id.backBtnRes);

        loginBtn.setOnClickListener(v->{
            Intent intent = new Intent(this, Login.class);
            intent.putExtra("type","restaurant");
            startActivity(intent);
        });
        registerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, RestaurantSignup.class);
            startActivity(intent);
        });

        backBtnRes.setOnClickListener(this::backRes);

    }

    public void backRes (View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}