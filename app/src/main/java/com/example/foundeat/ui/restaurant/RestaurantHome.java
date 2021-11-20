package com.example.foundeat.ui.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class RestaurantHome extends AppCompatActivity {
    private Restaurant restaurant;

    private TextView nameET;
    private Button resLogoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_home);

        restaurant = (Restaurant) getIntent().getExtras().get("restaurant");
        nameET = findViewById(R.id.nameTV);
        nameET.setText(restaurant.getName());
        resLogoutBtn = findViewById(R.id.resLogoutBtn);
        resLogoutBtn.setOnClickListener(this::logout);
    }

    private void logout(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        getSharedPreferences("foundEat", MODE_PRIVATE).edit().clear().apply();
        FirebaseAuth.getInstance().signOut();
    }
}