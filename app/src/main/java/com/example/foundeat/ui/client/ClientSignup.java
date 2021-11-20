package com.example.foundeat.ui.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Locale;
import java.util.UUID;

public class ClientSignup extends AppCompatActivity {

    private EditText clientNameET, clientEmailET, clientPassET, clientConfirmPassET;
    private Button clientRegisterBtn;
    private TextView clientLogin;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_signup);

        clientNameET = findViewById(R.id.clientNameET);
        clientEmailET = findViewById(R.id.clientEmailET);
        clientPassET = findViewById(R.id.clientPassET);
        clientConfirmPassET = findViewById(R.id.clientConfirmPassET);
        clientRegisterBtn = findViewById(R.id.clientRegisterBtn);
        clientLogin = findViewById(R.id.clientLogin);


        clientLogin.setOnClickListener(v->{
            Intent intent = new Intent(this, Login.class);
            intent.putExtra("type", "client");
            startActivity(intent);
        });

        clientRegisterBtn.setOnClickListener(this::signup);
    }
    private void signup(View view) {
        if (clientPassET.getText().toString().equals(clientConfirmPassET.getText().toString())) {

            //1. Registrar en db de Auth
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    clientEmailET.getText().toString().toLowerCase(Locale.ROOT),
                    clientPassET.getText().toString()
            ).addOnSuccessListener(v -> {
                //2. Resgistrar en la base de datos general
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                client = new Client();
                client.setId(firebaseUser.getUid());
                client.setName(clientNameET.getText().toString());
                client.setEmail(clientEmailET.getText().toString().toLowerCase(Locale.ROOT));

                db.collection("users").document(client.getId()).set(client).addOnSuccessListener(fireTask->{
                    sendVerificationEmail();
                    Intent intent = new Intent(this, Login.class);
                    intent.putExtra("type", "client");
                    startActivity(intent);
                });


            }).addOnFailureListener(error -> {
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
            });
        } else {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
        }
    }

    private void sendVerificationEmail() {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnSuccessListener(task->{
            Toast.makeText(this, "Se ha enviado un correo de verificación a la dirección dada", Toast.LENGTH_LONG).show();
        }).addOnFailureListener(error->{
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
        });
    }
}