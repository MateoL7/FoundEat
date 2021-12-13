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
import com.example.foundeat.model.MenuItem;
import com.example.foundeat.model.Restaurant;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class MenuListActivity extends AppCompatActivity {

    private Button addMenuItemBtn;
    private ImageButton goBackMTH;

    private String newItemName,newItemPrice,newItemDescription, menuItemImage;
    private RecyclerView menuListRV;

    private ActivityResultLauncher<Intent> launcher;

    //Model
    private Restaurant restaurant;
    private MenuItem item;
    private boolean editing;

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

        //Get editing from intent

        //Get model object from intent
        restaurant = (Restaurant) getIntent().getExtras().get("restaurant");

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

        adapter.setLauncher(launcher);
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
            editing = result.getData().getBooleanExtra("editing", false);
            if(!editing){
                newItemName = result.getData().getExtras().getString("nombre");
                newItemPrice = result.getData().getExtras().getString("precio");
                newItemDescription = result.getData().getExtras().getString("descripcion");
                menuItemImage=result.getData().getExtras().getString("MenuItemsPhoto");
                //(String id, String image, String name, String price, String description)
                MenuItem item = new MenuItem(UUID.randomUUID().toString(), menuItemImage, newItemName, newItemPrice, newItemDescription);
                adapter.addMenuItem(item);
                FirebaseFirestore.getInstance().collection("restaurants").document(restaurant.getId()).collection("menu").document(item.getId()).set(item);
            } else {
                item = (MenuItem) result.getData().getExtras().get("menuItem");
                item.setName(result.getData().getExtras().getString("nombre"));
                item.setPrice(result.getData().getExtras().getString("precio"));
                item.setDescription(result.getData().getExtras().getString("descripcion"));
                item.setImage(item.getImage());
                FirebaseFirestore.getInstance().collection("restaurants").document(restaurant.getId()).collection("menu").document(item.getId()).set(item);
            }
            adapter.clear();
            cargarMenu();
        }
    }

    public void cargarMenu(){
        FirebaseFirestore.getInstance().collection("restaurants").document(restaurant.getId()).collection("menu").get().addOnCompleteListener(
                task -> {
                    adapter.getMenuItems().clear();
                    adapter.notifyDataSetChanged();
                    for (DocumentSnapshot doc:task.getResult()){
                        MenuItem item = doc.toObject(MenuItem.class);
                        adapter.addMenuItem(item);
                    }
                }
        );
    }

}