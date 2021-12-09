package com.example.foundeat.ui.client.restaurantList;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.foundeat.R;
import com.example.foundeat.model.Restaurant;

public class ListsRestaurantsClients extends AppCompatActivity {

    private Restaurant restaurant;
    private TextView infoTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists_restaurants_clients);
        infoTest=findViewById(R.id.intoTest);
        restaurant=(Restaurant) getIntent().getExtras().get("restaurant");
        infoTest.setText("info restaurante: "+restaurant.getName()+" - "+restaurant.getCategory());
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}