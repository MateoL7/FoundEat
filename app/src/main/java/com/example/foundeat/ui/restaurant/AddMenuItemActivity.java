package com.example.foundeat.ui.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.foundeat.R;

public class AddMenuItemActivity extends AppCompatActivity {
    private ImageView photoMenuItemIV;
    private ImageButton addPhotoBtn, saveMenuItemBtn;
    private EditText menuItemProductNameET, priceMenuItemET, productDescriptionET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);
        photoMenuItemIV = findViewById(R.id.photoMenuItemIV);
        addPhotoBtn = findViewById(R.id.addPhotoBtn);
        saveMenuItemBtn = findViewById(R.id.saveMenuItemBtn);
        menuItemProductNameET = findViewById(R.id.menuItemProductNameET);
        priceMenuItemET = findViewById(R.id.priceMenuItemET);
        productDescriptionET = findViewById(R.id.productDescriptionET);

        saveMenuItemBtn.setOnClickListener(this::saveItem);
    }


    private void saveItem(View view){
        Intent intent = new Intent();

        //TODO pass image
        String nombre = menuItemProductNameET.getText().toString();
        String precio = priceMenuItemET.getText().toString();
        String descripcion = productDescriptionET.getText().toString();

        boolean finished = true;

        if(!nombre.equals("")){
            intent.putExtra("nombre", nombre);
        }else{
            Toast.makeText(getApplicationContext(),"Por favor introduzca un nombre",Toast.LENGTH_SHORT).show();
            finished = false;
        }
        if(!precio.equals("")){
            intent.putExtra("precio", precio);
        }else {
            Toast.makeText(getApplicationContext(),"Por favor introduzca un precio",Toast.LENGTH_SHORT).show();
            finished = false;
        }
        if(!descripcion.equals("")){
            intent.putExtra("descripcion", descripcion);
        }else{
            Toast.makeText(getApplicationContext(),"Por favor introduzca una descripcion",Toast.LENGTH_SHORT).show();
            finished = false;
        }

        if (finished){
            setResult(RESULT_OK, intent);
                finish();
        }
    }
}