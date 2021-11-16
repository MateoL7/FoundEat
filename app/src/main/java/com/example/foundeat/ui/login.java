package com.example.foundeat.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class login extends AppCompatActivity {

    private String type;
    private EditText emailET, passwordET;
    private Button goBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        type = getIntent().getExtras().get("type").toString();
        Log.e(">>>", type);

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordEt);
        goBtn = findViewById(R.id.goBtn);

        goBtn.setOnClickListener(v -> {
            String email = emailET.getText().toString();
            //Falta comprobación de contraseña

            String path="";

            if (type.equalsIgnoreCase("client")) {
                path = "users";
            } else if (type.equalsIgnoreCase("restaurant")) {
                path = "restaurants";
            }
            Query query = FirebaseFirestore.getInstance().collection(path).whereEqualTo("email", email);
            query.get().addOnCompleteListener(
                    task -> {
                        if (task.getResult().size() == 0) {
                            Toast.makeText(this,"Los datos ingresados no son correctos",Toast.LENGTH_LONG).show();
                        }
                        else {
                            if (type.equalsIgnoreCase("client")) {
                                Client client = null;
                                for (DocumentSnapshot doc : task.getResult()) {
                                    client = doc.toObject(Client.class);
                                    break;
                                }

                            } else if (type.equalsIgnoreCase("restaurant")) {
                                Restaurant restaurant = null;
                                for (DocumentSnapshot doc : task.getResult()) {
                                    restaurant = doc.toObject(Restaurant.class);
                                    break;
                                }
                                Intent intent = new Intent(this, RestaurantHome.class);
                                intent.putExtra("restaurant", restaurant);
                                startActivity(intent);
                            }


                        }

                    }
            );
        });


    }
}