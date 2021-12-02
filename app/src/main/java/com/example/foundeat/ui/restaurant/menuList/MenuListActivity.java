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
import android.widget.Button;
import android.widget.ImageButton;

import com.example.foundeat.R;
import com.example.foundeat.model.Restaurant;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.UUID;

public class MenuListActivity extends AppCompatActivity {

    private Button addMenuItemBtn;
    private ImageButton goBackMTH;

    private String newItemName,newItemPrice,newItemDescription, menuItemImage;
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
        goBackMTH = findViewById(R.id.goBackMTH);

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
        goBackMTH.setOnClickListener(this::goBackTo);

        //Print all menu items from database
        cargarMenu();

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

    private void goBackTo(View view) {
        finish();
    }

    //Method to call after AddMenuActivity has finished.
    public void onResult(ActivityResult result){
        //vaciarListaRecyclerView();
        if(result.getResultCode() == RESULT_OK){
            //If results are ok (menu item has info to add)

            newItemName = result.getData().getExtras().getString("nombre");
            newItemPrice = result.getData().getExtras().getString("precio");
            newItemDescription = result.getData().getExtras().getString("descripcion");
            menuItemImage=result.getData().getExtras().getString("MenuItemsPhoto");
            //(String id, String image, String name, String price, String description)
            MenuItemModel item = new MenuItemModel(UUID.randomUUID().toString(), menuItemImage, newItemName, newItemPrice, newItemDescription);
            adapter.addMenuItem(item);
            FirebaseFirestore.getInstance().collection("restaurants").document(restaurant.getId()).collection("menu").document(item.getId()).set(item);
            //cargarMenu();
        }
    }

//    public void vaciarListaRecyclerView(){
//        adapter.vaciarLista();
//    }
    public void cargarMenu(){
        FirebaseFirestore.getInstance().collection("restaurants").document(restaurant.getId()).collection("menu").get().addOnCompleteListener(
                task -> {
                    for (DocumentSnapshot doc:task.getResult()){
                        MenuItemModel item = doc.toObject(MenuItemModel.class);
                        adapter.addMenuItem(item);
                    }
                }
        );
    }

}