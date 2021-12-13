package com.example.foundeat.ui.restaurant;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.example.foundeat.R;
import com.example.foundeat.model.FoodCategory;
import com.example.foundeat.model.Restaurant;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Calendar;

public class RestaurantEditProfile extends AppCompatActivity {

    private AutoCompleteTextView categoryChoice;
    private ArrayList<FoodCategory> categories;
    private ImageView profilePics;
    private EditText descriptionET, closingET, openingET, addressET;
    private Button saveBtn;
    private ImageButton goBackTo;

    private Restaurant restaurant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_edit_profile);

        restaurant = (Restaurant) getIntent().getExtras().get("restaurant");

        descriptionET = findViewById(R.id.newNameET);
        closingET = findViewById(R.id.confirmNewPassword);
        openingET = findViewById(R.id.newPasswordET);
        addressET = findViewById(R.id.newEmailET);
        categoryChoice = findViewById(R.id.categoryChoice);
        saveBtn = findViewById(R.id.saveBtn);
        goBackTo = findViewById(R.id.goBackTo);

        ActivityResultLauncher<Intent> launcherMap = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::pickLocation
        );

        //Sujeto a cambios
        profilePics = findViewById(R.id.newPP);

        loadActualInfo();
        loadChoices();

        closingET.setOnClickListener(this::pickTime);
        openingET.setOnClickListener(this::pickTime);

        saveBtn.setOnClickListener(this::saveInfo);
        goBackTo.setOnClickListener(this::goBack);
        addressET.setOnClickListener(
                v -> {

                    Intent i = new Intent(this, RestaurantPickLocation.class);
                    launcherMap.launch(i);
                }
        );

    }

    private void pickTime(View v){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(RestaurantEditProfile.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                switch(v.getId()){
                    case R.id.newPasswordET:
                        String hora = String.format("%02d", selectedHour);
                        String minuto = String.format("%02d", selectedMinute);
                        openingET.setText( hora + ":" + minuto);
                        break;
                    case R.id.confirmNewPassword:
                        String hora2 = String.format("%02d", selectedHour);
                        String minuto2 = String.format("%02d", selectedMinute);
                        closingET.setText( hora2 + ":" + minuto2);
                        break;
                }
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void pickLocation(ActivityResult result) {

        if (result.getResultCode() == RESULT_OK) {
            String dir = result.getData().getExtras().getString("location");
            addressET.setText(dir);
        }
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
        ArrayAdapter<FoodCategory> adapter = new ArrayAdapter<>(this, R.layout.list_item, categories);
        categoryChoice.setAdapter(adapter);
        categoryChoice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FoodCategory foodCategory = (FoodCategory) parent.getItemAtPosition(position);
                restaurant.setCategory(foodCategory.toString());
            }
        });
    }

    private void saveInfo(View view) {
        restaurant.setDescription(descriptionET.getText().toString());
        restaurant.setAddress(addressET.getText().toString());
        restaurant.setOpeningTime(openingET.getText().toString());
        restaurant.setClosingTime(closingET.getText().toString());
        FirebaseFirestore.getInstance().collection("restaurants").document(restaurant.getId()).set(restaurant);
        Intent intent = new Intent(this, RestaurantHome.class);
        intent.putExtra("restaurant", restaurant);
        startActivity(intent);
        finish();
    }

    private void loadActualInfo() {
        loadPhoto();
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
        if (restaurant.getClosingTime() != null) {
            closingET.setText(restaurant.getClosingTime().toString());
        }
    }

    private void loadPhoto(){
        if(restaurant.getPics().size()>0){
            FirebaseStorage.getInstance().getReference().child("restaurantPhotos").child(restaurant.getPics().get(0)).getDownloadUrl().addOnSuccessListener(
                    url->   {
                        Glide.with(profilePics).load(url).into(profilePics);
                    }
            );
        }
    }

    public void goBack (View view) {
        finish();
    }
}