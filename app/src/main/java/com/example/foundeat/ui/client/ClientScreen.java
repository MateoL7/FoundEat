package com.example.foundeat.ui.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.foundeat.R;
import com.example.foundeat.ui.Login;
import com.example.foundeat.ui.MainActivity;

public class ClientScreen extends AppCompatActivity {

    private Button loginBtn, registerBtn;
    private ImageButton goBackAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_screen);

        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        goBackAll = findViewById(R.id.goBackAll);

        goBackAll.setOnClickListener(v-> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

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