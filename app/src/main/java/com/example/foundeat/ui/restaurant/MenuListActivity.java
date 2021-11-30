package com.example.foundeat.ui.restaurant;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.foundeat.R;

public class MenuListActivity extends AppCompatActivity {

    private ImageButton addMenuItemBtn;

    private String newItemName,newItemPrice,newItemDescription;

    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);
        addMenuItemBtn = findViewById(R.id.addMenuItemBtn);
        addMenuItemBtn.setOnClickListener(this::addMenuItem);

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
        }
    }
}