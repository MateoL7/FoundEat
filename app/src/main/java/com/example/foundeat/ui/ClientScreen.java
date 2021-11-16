package com.example.foundeat.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.foundeat.R;

public class ClientScreen extends AppCompatActivity {

    private Button loginBtn, registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_screen);

        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);

        loginBtn.setOnClickListener(v->{
            Intent intent = new Intent(this, Login.class);
            intent.putExtra("type","client");
            startActivity(intent);
        });
        registerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClientSignup.class);
            startActivity(intent);
        });

    }
}