package com.example.foundeat.ui.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.foundeat.R;
import com.example.foundeat.model.Restaurant;
import com.google.firebase.firestore.FirebaseFirestore;

public class RestaurantDescription extends AppCompatActivity {

    private TextView resGreetingTV, skipTV;
    private EditText descriptionET;
    private Button continueBtn;

    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_description);

        restaurant = (Restaurant) getIntent().getExtras().get("restaurant");

        resGreetingTV = findViewById(R.id.resGreetingTV);
        skipTV = findViewById(R.id.logoutTV);
        descriptionET = findViewById(R.id.descriptionET);
        continueBtn = findViewById(R.id.saveBtn);

        resGreetingTV.setText("Hola "+restaurant.getName()+",\neditaremos tu perfil para que los clientes te encuentren");
        continueBtn.setEnabled(false);

        descriptionET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                restaurant.setDescription(descriptionET.getText().toString());
                continueBtn.setEnabled(true);
            }
        });

        skipTV.setOnClickListener(v->{
            restaurant.setDescription(null);
            nextActivity(v);
        });
        continueBtn.setOnClickListener(this::nextActivity);


    }

    private void nextActivity(View view) {
        FirebaseFirestore.getInstance().collection("restaurants").document(restaurant.getId()).set(restaurant);
        Intent intent = new Intent(this, RestaurantMoreInfo.class);
        intent.putExtra("restaurant", restaurant);
        startActivity(intent);
    }
}