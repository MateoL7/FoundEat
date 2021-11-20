package com.example.foundeat.ui.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foundeat.R;
import com.example.foundeat.model.FoodCategory;
import com.example.foundeat.model.Restaurant;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RestaurantMoreInfo extends AppCompatActivity {

    private Spinner spinner;
    private EditText maxET, minET, closingET, openingET, addressET;
    private TextView skipTV;
    private Button continueBtn;

    private Restaurant restaurant;
    private ArrayList<FoodCategory> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_more_info);

        restaurant = (Restaurant) getIntent().getExtras().get("restaurant");

        skipTV = findViewById(R.id.skipTV);
        continueBtn = findViewById(R.id.continueBtn);
        maxET = findViewById(R.id.maxET);
        minET = findViewById(R.id.minET);
        closingET = findViewById(R.id.closingET);
        openingET = findViewById(R.id.openingET);
        addressET = findViewById(R.id.addressET);

        spinner = findViewById(R.id.spinner);
        loadSpinner();

        skipTV.setOnClickListener(v -> {
            nextActivitySkip(v);
        });

        continueBtn.setOnClickListener(this::nextActivity);
    }

    private void nextActivitySkip(View v) {
        restaurant.setCategory(null);
        restaurant.setCategory(null);
        restaurant.setMaxPrice(null);
        restaurant.setMinPrice(null);
        restaurant.setAddress(null);
        restaurant.setOpeningTime(null);
        restaurant.setClosingTime(null);
        Intent intent = new Intent(this, RestaurantHome.class);
        intent.putExtra("restaurant", restaurant);
        startActivity(intent);
    }

    private void loadSpinner() {
        categories = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("food").orderBy("category").addSnapshotListener(((value, error) -> {
            categories.clear();
            for (DocumentSnapshot doc : value.getDocuments()) {
                FoodCategory category = doc.toObject(FoodCategory.class);
                categories.add(category);
            }
        }));
        ArrayAdapter<FoodCategory> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //ESTO AÚN NO FUNCIONA, NO COGE EL ITEM SELECTED
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FoodCategory foodCategory = (FoodCategory) parent.getSelectedItem();
                restaurant.setCategory(foodCategory.getCategory());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void nextActivity(View view) {
        restaurant.setAddress(addressET.getText().toString());
        // FALTA CONVERTIR LA HORA DE APERTURA Y CIERRE
        restaurant.setMinPrice(minET.getText().toString());
        restaurant.setMaxPrice(maxET.getText().toString());
        FirebaseFirestore.getInstance().collection("restaurants").document(restaurant.getId()).set(restaurant);
        Intent intent = new Intent(this, RestaurantHome.class);
        intent.putExtra("restaurant", restaurant);
        startActivity(intent);
    }
}