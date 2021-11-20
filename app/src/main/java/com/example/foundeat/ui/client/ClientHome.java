package com.example.foundeat.ui.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ClientHome extends AppCompatActivity {

    private Client client;

    private TextView nameET;
    private Button clientLogoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        client = (Client) getIntent().getExtras().get("client");
        nameET = findViewById(R.id.nameTV);
        nameET.setText(client.getName());
        clientLogoutBtn = findViewById(R.id.clientLogoutBtn);
        clientLogoutBtn.setOnClickListener(this::logout);
    }

    private void logout(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        getSharedPreferences("foundEat", MODE_PRIVATE).edit().clear().apply();
        FirebaseAuth.getInstance().signOut();
    }
}