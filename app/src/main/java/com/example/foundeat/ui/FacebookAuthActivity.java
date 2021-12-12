package com.example.foundeat.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.client.ClientFavoriteFood;
import com.example.foundeat.ui.client.ClientHome;
import com.example.foundeat.ui.client.ClientPhoto;
import com.example.foundeat.ui.restaurant.RestaurantDescription;
import com.example.foundeat.ui.restaurant.RestaurantHome;
import com.example.foundeat.ui.restaurant.RestaurantMoreInfo;
import com.example.foundeat.ui.restaurant.RestaurantPhoto;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Locale;

public class FacebookAuthActivity extends Login {

    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private String type, path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_auth);

        type = getIntent().getExtras().get("type").toString();
        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Intent intent = new Intent(getBaseContext(),Login.class);
                        intent.putExtra("type",type);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        login(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(FacebookAuthActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createUser(FirebaseUser user) {
        if (type.equalsIgnoreCase("client")) {
            path = "users";
            Client client = new Client();
            client.setId(user.getUid());
            client.setName(user.getDisplayName());
            client.setEmail("Sin correo electrónico");
            client.setAccountInfo("Facebook");
            FirebaseFirestore.getInstance().collection("users").document(client.getId()).set(client).addOnSuccessListener(v -> {
                login(user);
            });
        } else if (type.equalsIgnoreCase("restaurant")) {
            path = "restaurants";
            Restaurant restaurant = new Restaurant();
            restaurant.setId(user.getUid());
            restaurant.setName(user.getDisplayName());
            restaurant.setEmail("Sin correo electrónico");
            restaurant.setAccountInfo("Facebook");
            FirebaseFirestore.getInstance().collection("restaurants").document(restaurant.getId()).set(restaurant).addOnSuccessListener(v -> {
                login(user);
            });
        }

    }

    private void login(FirebaseUser user) {
        if (type.equalsIgnoreCase("client")) {
            path = "users";
        } else if (type.equalsIgnoreCase("restaurant")) {
            path = "restaurants";
        }
        FirebaseFirestore.getInstance().collection(path).document(user.getUid()).get().addOnSuccessListener(document -> {
            if (type.equalsIgnoreCase("client")) {
                Client client1 = document.toObject(Client.class);
                if (client1 == null) {
                    createUser(user);
                } else {
                    Intent intent;
//                                 Para que termine el perfil si no lo ha terminado
                    if (client1.getProfilePic() == null) {
                        intent = new Intent(this, ClientPhoto.class);
                    } else if (client1.getFavoriteFood() == null || client1.getFavoriteFood().size() == 0) {
                        intent = new Intent(this, ClientFavoriteFood.class);
                    } else {
                        intent = new Intent(this, ClientHome.class);
                    }
                    intent.putExtra("client", client1);
                    startActivity(intent);
                }
            } else if (type.equalsIgnoreCase("restaurant")) {
                Restaurant restaurant = document.toObject(Restaurant.class);
                if (restaurant == null) {
                    createUser(user);
                }else{
                    Intent intent;

                    // Para que termine el perfil si no lo ha terminado
                    if (restaurant.getDescription() == null || restaurant.getDescription().isEmpty()) {
                        intent = new Intent(this, RestaurantDescription.class);
                    } else if (restaurant.getPics() == null || restaurant.getPics().size() < 1) {
                        intent = new Intent(this, RestaurantPhoto.class);
                    } else if (restaurant.getAddress() == null || restaurant.getOpeningTime() == null || restaurant.getClosingTime() == null) {
                        intent = new Intent(this, RestaurantMoreInfo.class);
                    } else {
                        intent = new Intent(this, RestaurantHome.class);
                    }
                    intent.putExtra("restaurant", restaurant);
                    startActivity(intent);
                }
            }
        }).addOnFailureListener(e -> {
            createUser(user);
        });
    }
}