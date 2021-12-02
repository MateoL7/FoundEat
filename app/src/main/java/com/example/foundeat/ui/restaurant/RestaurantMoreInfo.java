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
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.foundeat.R;
import com.example.foundeat.model.FoodCategory;
import com.example.foundeat.model.Restaurant;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

public class RestaurantMoreInfo extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private AutoCompleteTextView categoryChoice;
    private EditText closingET, openingET, addressET;
    private TextView skipTV;
    private Button continueBtn;

    private Restaurant restaurant;
    private ArrayList<FoodCategory> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_more_info);

        restaurant = (Restaurant) getIntent().getExtras().get("restaurant");

        skipTV = findViewById(R.id.logoutTV);
        continueBtn = findViewById(R.id.saveBtn);
        closingET = findViewById(R.id.closingET);
        openingET = findViewById(R.id.openingET);
        addressET = findViewById(R.id.addressET);

        ActivityResultLauncher<Intent> launcherMap = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::pickLocation
        );

        categoryChoice = findViewById(R.id.categoryChoice);
        loadActualInfo();
        loadChoices();

        skipTV.setOnClickListener(v -> {
            nextActivitySkip(v);
        });

        addressET.setOnClickListener(
                v->{

                    Intent i = new Intent(this, RestaurantPickLocation.class);
                    launcherMap.launch(i);
                }
        );

        closingET.setOnClickListener(this::pickTime);
        openingET.setOnClickListener(this::pickTime);

        continueBtn.setOnClickListener(this::nextActivity);
    }


    private void pickTime(View v){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(RestaurantMoreInfo.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                switch(v.getId()){
                    case R.id.openingET:
                        openingET.setText( selectedHour + ":" + selectedMinute);
                        break;
                    case R.id.closingET:
                        closingET.setText( selectedHour + ":" + selectedMinute);
                        break;
                }
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void pickLocation(ActivityResult result) {

        if(result.getResultCode()==RESULT_OK){
            String dir = result.getData().getExtras().getString("location");
            addressET.setText(dir);
        }
    }

    private void loadActualInfo() {
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
        categoryChoice.setOnItemClickListener(this);
    }

    private void nextActivity(View view) {
        restaurant.setAddress(addressET.getText().toString());
        restaurant.setOpeningTime(openingET.getText().toString());
        restaurant.setClosingTime(closingET.getText().toString());
        FirebaseFirestore.getInstance().collection("restaurants").document(restaurant.getId()).set(restaurant);
        Intent intent = new Intent(this, RestaurantHome.class);
        intent.putExtra("restaurant", restaurant);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FoodCategory foodCategory = (FoodCategory) parent.getItemAtPosition(position);
        restaurant.setCategory(foodCategory.toString());
    }
}