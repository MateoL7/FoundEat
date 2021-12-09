package com.example.foundeat.ui.client.restaurantList;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.foundeat.R;
import com.example.foundeat.ui.client.restaurantList.RestaurantListModel;

public class ListsRestaurantsClients extends AppCompatActivity {

    private RestaurantListModel restaurantListModel;
    private TextView infoTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists_restaurants_clients);
        infoTest=findViewById(R.id.intoTest);
        restaurantListModel=(RestaurantListModel)getIntent().getExtras().get("restaurant");
        infoTest.setText("info restaurante: "+restaurantListModel.getName()+" - "+restaurantListModel.getCategory());
    }

    public void setRestaurantListModel(RestaurantListModel restaurantListModel) {
        this.restaurantListModel = restaurantListModel;
    }
}