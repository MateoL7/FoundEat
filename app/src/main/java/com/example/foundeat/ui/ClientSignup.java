package com.example.foundeat.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;
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

        clientRegisterBtn.setOnClickListener(v -> {
            client = new Client();
            client.setId(UUID.randomUUID().toString());
            client.setName(clientNameET.getText().toString());
            client.setEmail(clientEmailET.getText().toString().toLowerCase(Locale.ROOT));
            client.setPassword(clientPassET.getText().toString());

            if (client.getPassword().equals(clientConfirmPassET.getText().toString())) {
                Query query = FirebaseFirestore.getInstance().collection("users").whereEqualTo("email", client.getEmail());
                query.get().addOnCompleteListener(
                        task -> {
                            if (task.getResult().size() == 0) {
                                //Indica que el email no ha sido registrado antes y se puede utilizar
                                db.collection("restaurants").document(client.getId()).set(client);
                                Intent intent = new Intent(this, ClientPhoto.class);
                                intent.putExtra("client", client);
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "Este email ya está registrado", Toast.LENGTH_LONG).show();
                            }
                        });
            }else{
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            }
        });
    }
}