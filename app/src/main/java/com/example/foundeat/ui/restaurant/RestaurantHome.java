package com.example.foundeat.ui.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foundeat.R;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.restaurant.menuList.MenuListActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;

public class RestaurantHome extends AppCompatActivity {
    private Restaurant restaurant;

    private TextView nameET,categoryTV,descriptionTV,addressTV,scheduleTV,priceTV,menuTV, reviewsTV;
    private ImageView actualPic;
    private Button editProfileBtn;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_home);

        restaurant = (Restaurant) getIntent().getExtras().get("restaurant");

        nameET = findViewById(R.id.nameTV);
        actualPic = findViewById(R.id.actualPic);
        categoryTV = findViewById(R.id.categoryTV);
        descriptionTV = findViewById(R.id.descriptionTV);
        addressTV = findViewById(R.id.addressTV);
        scheduleTV = findViewById(R.id.scheduleTV);
        priceTV = findViewById(R.id.priceTV);
        menuTV = findViewById(R.id.menuTV);
        reviewsTV = findViewById(R.id.reviewsTV);

        loadProfileInfo();

        editProfileBtn = findViewById(R.id.editProfileBtn);
        editProfileBtn.setOnClickListener(this::editProfile);
        menuTV.setOnClickListener(this::editMenu);
        reviewsTV.setOnClickListener(this::showReviews);
    }

    private void showReviews(View view) {
        Intent intent = new Intent(this,RestaurantReviews.class);
        intent.putExtra("restaurant",restaurant);
        startActivity(intent);
    }

    private void loadProfileInfo() {
        Query query =  db.collection("restaurants").whereEqualTo("id",restaurant.getId());
        query.get().addOnCompleteListener(task->{
            if (task.getResult().size() > 0) {
                for (DocumentSnapshot doc : task.getResult()) {
                    restaurant = doc.toObject(Restaurant.class);
                    break;
                }
                nameET.setText("Perfil\n" + restaurant.getName());

                if (restaurant.getPics() != null && restaurant.getPics().size() > 0) {
                    Log.e(">>>>>>", "HOLAAAAA >>> " + restaurant.getPics().get(0));
                    Bitmap bitmap = BitmapFactory.decodeFile(restaurant.getPics().get(0));
                    Bitmap thumbnail = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 4, bitmap.getHeight() / 4, true);
                    actualPic.setImageBitmap(thumbnail);
                }
                if (restaurant.getCategory() != null) {
                    categoryTV.setText(restaurant.getCategory());
                }
                if (restaurant.getDescription() != null) {
                    descriptionTV.setText(restaurant.getDescription());
                }
                if (restaurant.getAddress() != null) {
                    addressTV.setText(restaurant.getAddress());
                }
                if (restaurant.getOpeningTime() != null && restaurant.getClosingTime() != null) {

                }
                if(restaurant.getMinPrice()!= null && restaurant.getMaxPrice()!=null){
                    String price = "Min $" + restaurant.getMinPrice() + " - Max $" + restaurant.getMaxPrice();
                    priceTV.setText(price);
                }
            }
        });
        saveRestaurant(restaurant);
    }

    private void editProfile(View view){
        Intent intent = new Intent(this, RestaurantEditProfile.class);
        intent.putExtra("restaurant",restaurant);
        startActivity(intent);
    }


    private void saveRestaurant(Restaurant restaurant){
        String json = new Gson().toJson(restaurant);
        getSharedPreferences("foundEat",MODE_PRIVATE).edit().putString("restaurant",json).apply();
    }

    private void editMenu(View view){
        Intent intent = new Intent(this, MenuListActivity.class);
        intent.putExtra("restaurant", restaurant);
        startActivity(intent);
    }
}