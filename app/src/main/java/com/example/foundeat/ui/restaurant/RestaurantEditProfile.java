package com.example.foundeat.ui.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foundeat.R;
import com.example.foundeat.model.FoodCategory;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RestaurantEditProfile extends AppCompatActivity {

    private AutoCompleteTextView categoryChoice;
    private ArrayList<FoodCategory> categories;
    private ImageView profilePics;
    private EditText descriptionET, maxET, minET, closingET, openingET, addressET;
    private TextView logoutTV;
    private Button saveBtn;

    private Restaurant restaurant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_edit_profile);

        restaurant =(Restaurant) getIntent().getExtras().get("restaurant");

        descriptionET = findViewById(R.id.descriptionET);
        maxET = findViewById(R.id.maxET);
        minET = findViewById(R.id.minET);
        closingET = findViewById(R.id.closingET);
        openingET = findViewById(R.id.openingET);
        addressET = findViewById(R.id.addressET);
        categoryChoice = findViewById(R.id.categoryChoice);
        logoutTV = findViewById(R.id.logoutTV);
        saveBtn = findViewById(R.id.saveBtn);


        //Sujeto a cambios
        profilePics = findViewById(R.id.profilePics);

        loadActualInfo();
        loadChoices();
        saveBtn.setOnClickListener(this::saveInfo);
        logoutTV.setOnClickListener(this::logout);
        addressET.setOnClickListener(this::pickLocation);

    }

    private void pickLocation(View view) {
        Intent i = new Intent(this, RestaurantPickLocation.class);
        startActivity(i);
    }

    private void loadChoices() {
        categories = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("food").orderBy("category").addSnapshotListener(((value, error) -> {
            categories.clear();
            for (DocumentSnapshot doc : value.getDocuments()) {
                FoodCategory category = doc.toObject(FoodCategory.class);
                categories.add(category);
            }
        }));
        ArrayAdapter<FoodCategory> adapter = new ArrayAdapter<>(this, R.layout.list_item,categories);
        categoryChoice.setAdapter(adapter);
        categoryChoice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FoodCategory foodCategory = (FoodCategory) parent.getItemAtPosition(position);
                restaurant.setCategory(foodCategory.toString());
            }
        });
    }

    private void logout(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        getSharedPreferences("foundEat", MODE_PRIVATE).edit().clear().apply();
        FirebaseAuth.getInstance().signOut();
    }

    private void saveInfo(View view) {
        restaurant.setDescription(descriptionET.getText().toString());
        restaurant.setAddress(addressET.getText().toString());
        // FALTA CONVERTIR LA HORA DE APERTURA Y CIERRE
        restaurant.setMinPrice(minET.getText().toString());
        restaurant.setMaxPrice(maxET.getText().toString());
        FirebaseFirestore.getInstance().collection("restaurants").document(restaurant.getId()).set(restaurant);
        Intent intent = new Intent(this, RestaurantHome.class);
        intent.putExtra("restaurant",restaurant);
        startActivity(intent);
        finish();
    }

    private void loadActualInfo() {
        if (restaurant.getPics() != null && restaurant.getPics().size() > 0) {
            Log.e(">>>>>>", "HOLAAAAA >>> " + restaurant.getPics().get(0));
            Bitmap bitmap = BitmapFactory.decodeFile(restaurant.getPics().get(0));
            Bitmap thumbnail = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 4, bitmap.getHeight() / 4, true);
            profilePics.setImageBitmap(thumbnail);
        }
        if (restaurant.getDescription() != null) {
            descriptionET.setText(restaurant.getDescription());
        }
        if (restaurant.getCategory() != null) {
            categoryChoice.setText(restaurant.getCategory());
        }
        if (restaurant.getAddress() != null) {
            addressET.setText(restaurant.getAddress());
        }
        if (restaurant.getOpeningTime() != null) {
            openingET.setText(restaurant.getOpeningTime().toString());
        }
        if(restaurant.getClosingTime() != null){
            closingET.setText(restaurant.getClosingTime().toString());
        }
        if(restaurant.getMinPrice()!= null){
            minET.setText(restaurant.getMinPrice());
        }
        if(restaurant.getMaxPrice()!=null){
            maxET.setText(restaurant.getMaxPrice());
        }
    }
}