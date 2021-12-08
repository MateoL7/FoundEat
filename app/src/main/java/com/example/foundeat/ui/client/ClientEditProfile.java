package com.example.foundeat.ui.client;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.ui.ChoiceDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientEditProfile extends AppCompatActivity implements ChoiceDialog.OnChoiceListener {

    private Client client;

    private EditText newNameET, newEmailET, newPasswordET, confirmNewPassword;
    private Button saveBtn;
    private ImageView newPP;
    private FirebaseUser actualUser;

    private Uri uri;
    private File file;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_edit_profile);

        client = (Client) getIntent().getExtras().get("client");
        actualUser = FirebaseAuth.getInstance().getCurrentUser();

        newNameET = findViewById(R.id.newNameET);
        newEmailET = findViewById(R.id.newEmailET);
        newPasswordET = findViewById(R.id.newPasswordET);
        confirmNewPassword = findViewById(R.id.confirmNewPassword);
        saveBtn = findViewById(R.id.saveBtn);
        newPP = findViewById(R.id.newPP);

        saveBtn.setOnClickListener(this::saveChanges);

        newNameET.setText(client.getName());
        newEmailET.setText(client.getEmail());

        loadPhoto();

        newPP.setOnClickListener(this::openChoice);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onCameraResult);
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onGalleryResult);

    }

    private void saveChanges(View view) {
        String actualName = client.getName();
        String actualEmail = client.getEmail();
        if (newNameET.getText().toString().isEmpty() || newEmailET.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor llene todos los campos", Toast.LENGTH_LONG).show();
        }
        else {
            if (!actualName.equals(newNameET.getText().toString())) {
                client.setName(newNameET.getText().toString());
            }
            if (!actualEmail.equalsIgnoreCase(newEmailET.getText().toString())) {
                actualUser.updateEmail(newEmailET.getText().toString()).
                        addOnSuccessListener(v -> {
                            client.setEmail(newEmailET.getText().toString());
                            if (!actualUser.isEmailVerified()) {
                                sendVerificationEmail();
                            }
                            updatePassword();
                        }).addOnFailureListener(error -> {
                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
                });
            }else{
               updatePassword();
            }
        }

    }

    private void saveAndChange(){
        saveNewPhoto();
        FirebaseFirestore.getInstance().collection("users").document(client.getId()).set(client);
        Intent intent = new Intent(this, ClientHome.class);
        intent.putExtra("client", client);
        startActivity(intent);
    }

    private void updatePassword(){
        if(newPasswordET.getText().toString().isEmpty() && confirmNewPassword.getText().toString().isEmpty()){
            saveAndChange();
        }else {
            if (newPasswordET.getText().toString().length() < 6) {
                Toast.makeText(this, "La contraseña debe contener al menos 6 caracteres", Toast.LENGTH_LONG).show();
            } else if (!newPasswordET.getText().toString().equals(confirmNewPassword.getText().toString())) {
                Toast.makeText(this, "Las contraseñas deben coincidir", Toast.LENGTH_LONG).show();
            } else {
                actualUser.updatePassword(newPasswordET.getText().toString());
                saveAndChange();
            }
        }
    }

    private void saveNewPhoto() {
        if(uri != null){
            if (client.getProfilePic() != null) {
                FirebaseStorage.getInstance().getReference().child("clientPhotos").child(client.getProfilePic()).putFile(uri);
            } else {
                String fileName = UUID.randomUUID().toString();
                FirebaseStorage.getInstance().getReference().child("clientPhotos").child(fileName).putFile(uri);
                client.setProfilePic(fileName);
            }
        }
    }

    private void loadPhoto() {
        if (client.getProfilePic() != null) {
            FirebaseStorage.getInstance().getReference().child("clientPhotos").child(client.getProfilePic()).getDownloadUrl().addOnSuccessListener(
                    url -> {
                        Glide.with(newPP).load(url).into(newPP);
                    }
            );
        }
    }

    private void sendVerificationEmail() {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnSuccessListener(task -> {
            Toast.makeText(this, "Se ha enviado un correo de verificación a la dirección dada", Toast.LENGTH_LONG).show();
        }).addOnFailureListener(error -> {
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    public void openChoice(View view) {
        ChoiceDialog dialog = ChoiceDialog.newInstance();
        dialog.setListener(this);
        dialog.show(this.getSupportFragmentManager(), "dialog");
    }

    public void onGalleryResult(ActivityResult result) {
        if (result.getResultCode() == this.RESULT_OK) {
            uri = result.getData().getData();
            newPP.setImageURI(uri);
            newPP.setBackground(null);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
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

            newPP.setImageBitmap(thumbnail);
            uri = FileProvider.getUriForFile(this, this.getPackageName(), file);
            newPP.setBackground(null);

        } else if (result.getResultCode() == this.RESULT_CANCELED) {
            Toast.makeText(this, "Operación cancelada", Toast.LENGTH_LONG).show();
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