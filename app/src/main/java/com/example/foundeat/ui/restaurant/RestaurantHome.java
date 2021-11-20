package com.example.foundeat.ui.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class RestaurantHome extends AppCompatActivity {
    private Restaurant restaurant;

    private TextView nameET,categoryTV,descriptionTV,addressTV,scheduleTV,priceTV;
    private ImageView actualPic;
    private Button resLogoutBtn;

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

        nameET.setText("Perfil\n"+restaurant.getName());

        loadProfileInfo();

        resLogoutBtn = findViewById(R.id.resLogoutBtn);
        resLogoutBtn.setOnClickListener(this::logout);
    }

    private void loadProfileInfo() {
        if(restaurant.getPics() != null && restaurant.getPics().size()>0){
            Log.e(">>>>>>","HOLAAAAA >>> "+restaurant.getPics().get(0));
            Bitmap bitmap = BitmapFactory.decodeFile(restaurant.getPics().get(0));
            Bitmap thumbnail = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/4,bitmap.getHeight()/4,true);
            actualPic.setImageBitmap(thumbnail);
        }
        if(restaurant.getCategory()!=null){
            categoryTV.setText(restaurant.getCategory());
        }
        if(restaurant.getDescription()!=null){
            descriptionTV.setText(restaurant.getDescription());
        }
        if(restaurant.getAddress()!=null){
            addressTV.setText(restaurant.getAddress());
        }
        if(restaurant.getOpeningTime()!=null && restaurant.getClosingTime()!= null){
            priceTV.setText("Min $"+ restaurant.getMinPrice()+" - Max $"+ restaurant.getMaxPrice());
        }
    }

    private void logout(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        getSharedPreferences("foundEat", MODE_PRIVATE).edit().clear().apply();
        FirebaseAuth.getInstance().signOut();
    }
}