package com.example.foundeat.ui.client.restaurantInfo.menuRV;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.MenuItem;
import com.example.foundeat.model.Restaurant;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ClientMenuList extends AppCompatActivity {

    private Restaurant restaurant;
    private Client client;

    //Needed for RecyclerView configuration
    private LinearLayoutManager manager;
    private MenuItemAdapter adapter;

    //View fields
    private ImageButton goBackMTH;
    private RecyclerView menuListRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_menu_list);
        menuListRV = findViewById(R.id.menuListRV);
        goBackMTH = findViewById(R.id.goBackMTH);

        //Recieve object from intent
        restaurant = (Restaurant) getIntent().getExtras().get("restaurant");
        client = (Client) getIntent().getExtras().get("client");

        manager = new LinearLayoutManager(this);
        menuListRV.setLayoutManager(manager);
        adapter = new MenuItemAdapter();
        menuListRV.setAdapter(adapter);
        menuListRV.setHasFixedSize(true);
        goBackMTH.setOnClickListener(this::returnToInfo);

        //Get Menu Items from DB
        loadMenuItems();
    }

    public void loadMenuItems(){
        FirebaseFirestore.getInstance().collection("restaurants").document(restaurant.getId()).collection("menu").get().addOnCompleteListener(
                task -> {
                    for (DocumentSnapshot doc:task.getResult()){
                        MenuItem item = doc.toObject(MenuItem.class);
                        adapter.addMenuItem(item);
                    }
                }
        );
    }

    public void returnToInfo(View v){

    }

}