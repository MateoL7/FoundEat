package com.example.foundeat.ui.restaurant.menuList;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.foundeat.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.UUID;

public class AddMenuItemActivity extends AppCompatActivity {
    //private ImageView photoMenuItemIV;
    private ImageButton addPhotoBtn, backAPBtn;
    private Button saveMenuItemBtn;
    private EditText menuItemProductNameET, priceMenuItemET, productDescriptionET;
    private Uri uri;

    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);
        //photoMenuItemIV = findViewById(R.id.photoMenuItemIV);
        addPhotoBtn = findViewById(R.id.addPhotoBtn);
        saveMenuItemBtn = findViewById(R.id.saveMenuItemBtn);
        menuItemProductNameET = findViewById(R.id.menuItemProductNameET);
        priceMenuItemET = findViewById(R.id.priceMenuItemET);
        productDescriptionET = findViewById(R.id.productDescriptionET);
        backAPBtn = findViewById(R.id.backAPBtn);

        launcher  =registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onGaleryResult);

        saveMenuItemBtn.setOnClickListener(this::saveItem);
        addPhotoBtn.setOnClickListener(this::addPhoto);
        backAPBtn.setOnClickListener(this::goToMenu);
    }

    public void onGaleryResult(ActivityResult result){

        if (result.getResultCode()== RESULT_OK){
            uri = result.getData().getData();
            addPhotoBtn.setImageURI(uri);
        }

    }

    public void addPhoto(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        launcher.launch(intent);
    }

    public void goToMenu (View view) {
        Intent intent = new Intent(this, MenuListActivity.class);
        startActivity(intent);
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
        if(uri != null){
            String fileName = UUID.randomUUID().toString();
            FirebaseStorage.getInstance().getReference().child("MenuItemsPhoto").child(fileName).putFile(uri);
            intent.putExtra("MenuItemsPhoto", fileName);
        }else{
            Toast.makeText(getApplicationContext(),"Por favor seleccione una foto",Toast.LENGTH_SHORT).show();
            finished = false;
        }
        if (finished){
            setResult(RESULT_OK, intent);
                finish();
        }
    }
}