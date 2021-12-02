package com.example.foundeat.ui.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foundeat.R;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.Login;
import com.example.foundeat.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Locale;
import java.util.UUID;

public class RestaurantSignup extends AppCompatActivity {

    private EditText resNameET, resEmailET, resPassET, resConfirmPassET;
    private Button resRegisterBtn;
    private TextView resLogin;
    private ImageButton backBtn;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_signup);

        resNameET = findViewById(R.id.clientNameET);
        resEmailET = findViewById(R.id.clientEmailET);
        resPassET = findViewById(R.id.clientPassET);
        resConfirmPassET = findViewById(R.id.clientConfirmPassET);
        resRegisterBtn = findViewById(R.id.clientRegisterBtn);
        resLogin = findViewById(R.id.clientLogin);
        backBtn = findViewById(R.id.backBtn);


        resLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, Login.class);
            intent.putExtra("type", "restaurant");
            startActivity(intent);
        });

        resRegisterBtn.setOnClickListener(this::signup);
        backBtn.setOnClickListener(this::goBack);
    }

    private void signup(View view) {
        if(resNameET.getText().toString().isEmpty() || resEmailET.getText().toString().isEmpty() || resPassET.getText().toString().isEmpty() || resConfirmPassET.getText().toString().isEmpty()){
            Toast.makeText(this,"Por favor llene todos los campos",Toast.LENGTH_LONG).show();
        }
        else if (resPassET.getText().toString().equals(resConfirmPassET.getText().toString())) {

            //1. Registrar en db de Auth
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    resEmailET.getText().toString().toLowerCase(Locale.ROOT),
                    resPassET.getText().toString()
            ).addOnSuccessListener(v -> {
                //2. Resgistrar en la base de datos general
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                restaurant = new Restaurant();
                restaurant.setId(firebaseUser.getUid());
                restaurant.setName(resNameET.getText().toString());
                restaurant.setEmail(resEmailET.getText().toString().toLowerCase(Locale.ROOT));

                db.collection("restaurants").document(restaurant.getId()).set(restaurant).addOnSuccessListener(fireTask->{
                    sendVerificationEmail();
                    Intent intent = new Intent(this, Login.class);
                    intent.putExtra("type", "restaurant");
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

    public void goBack (View view) {
        Intent intent = new Intent(this, RestaurantScreen.class);
        startActivity(intent);

    }
}