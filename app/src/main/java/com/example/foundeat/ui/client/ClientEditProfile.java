package com.example.foundeat.ui.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.atomic.AtomicBoolean;

public class ClientEditProfile extends AppCompatActivity {

    private Client client;

    private EditText newNameET, newEmailET, newPasswordET, confirmNewPassword;
    private Button saveBtn;
    private ImageView newPP;
    private FirebaseUser actualUser;

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


    }

    private void saveChanges(View view) {
        if (newNameET.getText().toString().isEmpty() || newEmailET.getText().toString().isEmpty() || newPasswordET.getText().toString().isEmpty() || confirmNewPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor llene todos los campos", Toast.LENGTH_LONG).show();
        } else if (!newPasswordET.getText().toString().equals(confirmNewPassword.getText().toString())) {
            Toast.makeText(this, "Las contraseñas deben coincidir", Toast.LENGTH_LONG).show();
        } else {
            client.setName(newNameET.getText().toString());
            if(newPasswordET.getText().toString().length()<6){
                Toast.makeText(this, "La contraseña debe contener al menos 6 caracteres", Toast.LENGTH_LONG).show();
            }else {
                actualUser.updatePassword(newPasswordET.getText().toString());
                actualUser.updateEmail(newEmailET.getText().toString()).
                        addOnSuccessListener(v -> {
                            client.setEmail(newEmailET.getText().toString());
                            if(!actualUser.isEmailVerified()){
                                sendVerificationEmail();
                            }
                            FirebaseFirestore.getInstance().collection("users").document(client.getId()).set(client);
                            Intent intent = new Intent(this,ClientHome.class);
                            intent.putExtra("client", client);
                            startActivity(intent);
                        }).addOnFailureListener(error -> {
                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }
    }

    private void sendVerificationEmail() {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnSuccessListener(task -> {
            Toast.makeText(this, "Se ha enviado un correo de verificación a la dirección dada", Toast.LENGTH_LONG).show();
        }).addOnFailureListener(error -> {
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
        });
    }
}