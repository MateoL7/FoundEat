package com.example.foundeat.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foundeat.R;
import com.example.foundeat.model.Restaurant;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Locale;
import java.util.UUID;

public class RestaurantRegister extends AppCompatActivity {

    private EditText resNameET, resEmailET, resPassET, resConfirmPassET;
    private Button resRegisterBtn;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_register);

        resNameET = findViewById(R.id.resNameET);
        resEmailET = findViewById(R.id.resEmailET);
        resPassET = findViewById(R.id.resPassET);
        resConfirmPassET = findViewById(R.id.resConfirmPassET);
        resRegisterBtn = findViewById(R.id.resRegisterBtn);

        resRegisterBtn.setOnClickListener(v -> {
            restaurant = new Restaurant();
            restaurant.setId(UUID.randomUUID().toString());
            restaurant.setName(resNameET.getText().toString());
            restaurant.setEmail(resEmailET.getText().toString().toLowerCase(Locale.ROOT));
            restaurant.setPassword(resPassET.getText().toString());

            if (restaurant.getPassword().equals(resConfirmPassET.getText().toString())) {
                Query query = FirebaseFirestore.getInstance().collection("restaurants").whereEqualTo("email", restaurant.getEmail());
                query.get().addOnCompleteListener(
                        task -> {
                            if (task.getResult().size() == 0) {
                                //Indica que el email no ha sido registrado antes y se puede utilizar
                                db.collection("restaurants").document(restaurant.getId()).set(restaurant);
                                Intent intent = new Intent(this, RestaurantMoreInfo.class);
                                intent.putExtra("restaurant", restaurant);
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