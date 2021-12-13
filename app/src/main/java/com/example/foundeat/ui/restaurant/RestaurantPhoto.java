package com.example.foundeat.ui.restaurant;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foundeat.R;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.ChoiceDialog;
import com.example.foundeat.util.UtilDomi;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class RestaurantPhoto extends AppCompatActivity implements ChoiceDialog.OnChoiceListener {

    // CAMBIAR ESTA CLASE PARA LA LOGICA DE LAS FOTOS CON FIREBASE

    private TextView skipPicTV, greetingTV;
    private ImageView profilePic;
    private Button continueBtn;

    private File file;
    private Uri uri;

    private Restaurant restaurant;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_photo);

        restaurant = (Restaurant) getIntent().getExtras().get("restaurant");

        greetingTV = findViewById(R.id.greetingTV);

        greetingTV.setText("Agrega 1 foto principal para tu perfil");

        profilePic = findViewById(R.id.profilePic);
        continueBtn = findViewById(R.id.saveBtn);
        skipPicTV = findViewById(R.id.logoutTV);

        skipPicTV.setOnClickListener(v -> {
            continueNoPhoto(v);
        });
        continueBtn.setOnClickListener(this::nextActivity);

        profilePic.setOnClickListener(this::openChoice);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onCameraResult);
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onGalleryResult);

    }

    private void continueNoPhoto(View v){
        Intent intent;
        if(restaurant.getAddress() == null ||restaurant.getOpeningTime() == null || restaurant.getClosingTime()== null){
            intent = new Intent(this, RestaurantMoreInfo.class);
        }else{
            intent = new Intent(this, RestaurantHome.class);
        }
        intent.putExtra("restaurant", restaurant);
        startActivity(intent);
    }

    public void openChoice(View view) {
        ChoiceDialog dialog = ChoiceDialog.newInstance();
        dialog.setListener(this);
        dialog.show(this.getSupportFragmentManager(), "dialog");
    }

    private void nextActivity(View view) {

        if(uri != null) {
            String fileName = UUID.randomUUID().toString();
            FirebaseStorage.getInstance().getReference().child("restaurantPhotos").child(fileName).putFile(uri);
            restaurant.getPics().add(fileName);
            FirebaseFirestore.getInstance().collection("restaurants").document(restaurant.getId()).set(restaurant);
            Intent intent;
            if(restaurant.getAddress() == null ||restaurant.getOpeningTime() == null || restaurant.getClosingTime()== null){
                intent = new Intent(this, RestaurantMoreInfo.class);
            }else{
                intent = new Intent(this, RestaurantHome.class);
            }
            intent.putExtra("restaurant", restaurant);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"Por favor seleccione una foto",Toast.LENGTH_SHORT).show();
        }
    }

    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(this.getExternalFilesDir(null) + "/photo.png");
        Uri uri = FileProvider.getUriForFile(this, this.getPackageName(), file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        cameraLauncher.launch(intent);
    }

    public void onCameraResult(ActivityResult result) {
        if (result.getResultCode() == this.RESULT_OK) {

            //Foto completa
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            Bitmap thumbnail = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 4, bitmap.getHeight() / 4, true);

            profilePic.setImageBitmap(thumbnail);
            uri = FileProvider.getUriForFile(this, this.getPackageName(), file);
            continueBtn.setEnabled(true);
            profilePic.setBackground(null);

        } else if (result.getResultCode() == this.RESULT_CANCELED) {
            Toast.makeText(this, "Operación cancelada", Toast.LENGTH_LONG).show();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    public void onGalleryResult(ActivityResult result) {
        if (result.getResultCode() == this.RESULT_OK) {
            uri = result.getData().getData();
            profilePic.setImageURI(uri);
            continueBtn.setEnabled(true);
            profilePic.setBackground(null);
        }
    }

    @Override
    public void onChoice(int choice) {
        if (choice == 0) {
            openCamera();
        } else if (choice == 1) {
            openGallery();
        }
    }
}