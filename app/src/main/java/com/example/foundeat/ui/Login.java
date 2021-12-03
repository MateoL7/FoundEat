package com.example.foundeat.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.client.ClientFavoriteFood;
import com.example.foundeat.ui.client.ClientHome;
import com.example.foundeat.ui.client.ClientPhoto;
import com.example.foundeat.ui.client.ClientSignup;
import com.example.foundeat.ui.restaurant.RestaurantDescription;
import com.example.foundeat.ui.restaurant.RestaurantHome;
import com.example.foundeat.ui.restaurant.RestaurantMoreInfo;
import com.example.foundeat.ui.restaurant.RestaurantPhoto;
import com.example.foundeat.ui.restaurant.RestaurantScreen;
import com.example.foundeat.ui.restaurant.RestaurantSignup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class Login extends AppCompatActivity {

    private String type;
    private String path="";

    private EditText emailET, passwordET;
    private Button goBtn;
    private TextView goToSignUp;
    private ImageButton backBtnL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        type = getIntent().getExtras().get("type").toString();

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        goBtn = findViewById(R.id.clientRegisterBtn);
        goToSignUp = findViewById(R.id.goToSignUp);
        backBtnL = findViewById(R.id.backBtnL);

        goToSignUp.setOnClickListener(v -> {
            if (type.equalsIgnoreCase("client")) {
                Intent intent = new Intent(this, ClientSignup.class);
                startActivity(intent);
            } else if (type.equalsIgnoreCase("restaurant")) {
                Intent intent = new Intent(this, RestaurantSignup.class);
                startActivity(intent);
            }
        });

        goBtn.setOnClickListener(this::login);
        backBtnL.setOnClickListener(this::goBackL);

    }

    private void login(View view){
        if (type.equalsIgnoreCase("client")) {
            path = "users";
        } else if (type.equalsIgnoreCase("restaurant")) {
            path = "restaurants";
        }

        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnSuccessListener(
                task->{
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if(firebaseUser.isEmailVerified()){
                        //Acceso

                        //Pedir usuario en firestore
                        FirebaseFirestore.getInstance().collection(path).document(firebaseUser.getUid()).get().addOnSuccessListener(document->{
                            if (type.equalsIgnoreCase("client")) {
                                Client client = document.toObject(Client.class);
                                Intent intent;

                                // Para que termine el perfil si no lo ha terminado
//                                if(client.getProfilePic()==null){
//                                    intent = new Intent(this, ClientPhoto.class);
//                                }
//                                else if(client.getFavoriteFood().length<3){
//                                    intent = new Intent(this, ClientFavoriteFood.class);
//                                }
//                                else{
//                                    intent = new Intent(this, ClientHome.class);
//                                }

                                intent = new Intent(this, ClientHome.class);
                                intent.putExtra("client", client);
                                startActivity(intent);
                            } else if (type.equalsIgnoreCase("restaurant")) {
                                Restaurant restaurant = document.toObject(Restaurant.class);
                                Intent intent;

                                // Para que termine el perfil si no lo ha terminado
                                if(restaurant.getDescription() == null || restaurant.getDescription().isEmpty()){
                                    intent = new Intent(this, RestaurantDescription.class);
                                }
                                else if(restaurant.getPics() == null || restaurant.getPics().size() < 1){
                                    intent = new Intent(this, RestaurantPhoto.class);
                                }else if(restaurant.getAddress() == null ||restaurant.getOpeningTime() == null || restaurant.getClosingTime()== null){
                                    intent = new Intent(this, RestaurantMoreInfo.class);
                                }else{
                                    intent = new Intent(this, RestaurantHome.class);
                                }
                                intent.putExtra("restaurant", restaurant);
                                startActivity(intent);
                            }
                        });
                    }else{
                        Toast.makeText(this, "Su email no ha sido verificado", Toast.LENGTH_LONG).show();
                    }
                }
        ).addOnFailureListener(error->{
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
        });

    }

    public void goBackL (View view) {
        Intent intent = new Intent(this, RestaurantScreen.class);
        startActivity(intent);
    }
}