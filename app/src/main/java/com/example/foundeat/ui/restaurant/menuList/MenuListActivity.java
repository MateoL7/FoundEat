package com.example.foundeat.ui.restaurant.menuList;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.foundeat.R;
import com.example.foundeat.model.Restaurant;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class MenuListActivity extends AppCompatActivity {

    private ImageButton addMenuItemBtn;

    private String newItemName,newItemPrice,newItemDescription;
    private RecyclerView menuListRV;

    private ActivityResultLauncher<Intent> launcher;

    //Model
    private Restaurant restaurant;

    //Needed for RecyclerView configuration
    private LinearLayoutManager manager;
    private MenuItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);
        //Reference items from ID
        addMenuItemBtn = findViewById(R.id.addMenuItemBtn);
        menuListRV = findViewById(R.id.menuListRV);

        //Get model object from intent
        restaurant = (Restaurant) getIntent().getExtras().get("restaurant");

        Log.e(">>>>>>Restaurant name", restaurant.getName());
        Log.e(">>>>>>Restaurant id", restaurant.getId());

        //Configure items that need configuration
        addMenuItemBtn.setOnClickListener(this::addMenuItem);
        manager = new LinearLayoutManager(this);
        menuListRV.setLayoutManager(manager);
        adapter = new MenuItemAdapter();
        menuListRV.setAdapter(adapter);
        menuListRV.setHasFixedSize(true);

        //Create callback launcher
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::onResult
        );
    }

    //Method that handles add item button
    private void addMenuItem(View v){
        Intent intent = new Intent(this, AddMenuItemActivity.class);
        launcher.launch(intent);
    }

    //Method to call after AddMenuActivity has finished.
    public void onResult(ActivityResult result){
        if(result.getResultCode() == RESULT_OK){
            //If results are ok (menu item has info to add)

            newItemName = result.getData().getExtras().getString("nombre");
            newItemPrice = result.getData().getExtras().getString("precio");
            newItemDescription = result.getData().getExtras().getString("descripcion");
            //(String id, String image, String name, String price, String description)
            //TODO Get image from gallery
            MenuItemModel item = new MenuItemModel(UUID.randomUUID().toString(), "Get image from gallery", newItemName, newItemPrice, newItemDescription);
            adapter.addMenuItem(item);
            FirebaseFirestore.getInstance().collection("restaurants").document(restaurant.getId()).collection("menu").document(item.getId()).set(item);
        }
    }
}