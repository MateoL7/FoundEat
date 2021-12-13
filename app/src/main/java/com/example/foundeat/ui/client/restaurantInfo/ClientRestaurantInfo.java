package com.example.foundeat.ui.client.restaurantInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.client.ClientAddReview;
import com.example.foundeat.ui.client.restaurantInfo.menuRV.ClientMenuList;
import com.example.foundeat.ui.restaurant.RestaurantLocation;
import com.example.foundeat.ui.restaurant.RestaurantReviews;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.UUID;

public class ClientRestaurantInfo extends AppCompatActivity {

    private Restaurant restaurant;
    private Client currentClient;

    private TextView nameTV;
    private ImageView restaurantPic;
    private TextView categoryTV;
    private TextView descriptionTV;
    private TextView addressTV;
    private TextView scheduleTV;
    private TextView pricesTV;
    private TextView menuTV;
    private TextView reviewsTV;
    private Button addReviewBtn;
    private ImageButton favBttn, goBack;

    private boolean added;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_restaurant_info);

        //Load view elements
        nameTV = findViewById(R.id.nameTV);
        restaurantPic = findViewById(R.id.pp);
        categoryTV = findViewById(R.id.userTV);
        descriptionTV = findViewById(R.id.descriptionTV);
        addressTV = findViewById(R.id.favoritesTV);
        scheduleTV = findViewById(R.id.myReviewsTV);
        pricesTV = findViewById(R.id.editProfileTV);
        menuTV = findViewById(R.id.menuTV);
        reviewsTV = findViewById(R.id.reviewsTV);
        addReviewBtn = findViewById(R.id.addReviewBtn);
        favBttn = findViewById(R.id.favBttn);
        goBack = findViewById(R.id.goBack);

        //Add listeners to buttons
        favBttn.setOnClickListener(this::addToFavorites);
        menuTV.setOnClickListener(this::loadMenu);
        goBack.setOnClickListener(this::goBackTo);
        addReviewBtn.setOnClickListener(this::addReview);
        reviewsTV.setOnClickListener(this::showReviews);

        //Get objects from intents
        restaurant=(Restaurant) getIntent().getExtras().get("restaurant");
        currentClient = (Client) getIntent().getExtras().get("client");

        //Load info into view
        loadRestaurantInfo();
    }

    private void showReviews(View view) {
        Intent intent = new Intent(this, RestaurantReviews.class);
        intent.putExtra("restaurant",restaurant);
        startActivity(intent);
    }

    private void addReview(View view) {
        Intent intent = new Intent(this, ClientAddReview.class);
        intent.putExtra("client",currentClient);
        intent.putExtra("restaurant",restaurant);
        startActivity(intent);
        finish();
    }

    public void loadRestaurantInfo(){
        //Load restaurant info from DB
        Query query =  db.collection("restaurants").whereEqualTo("id",restaurant.getId());
        query.get().addOnCompleteListener(task->{
            if (task.getResult().size() > 0) {
                for (DocumentSnapshot doc : task.getResult()) {
                    restaurant = doc.toObject(Restaurant.class);
                    break;
                }
                nameTV.setText("Perfil\n" + restaurant.getName());

                if (restaurant.getPics() != null && restaurant.getPics().size() > 0) {
                    FirebaseStorage.getInstance().getReference().child("restaurantPhotos").child(restaurant.getPics().get(0)).getDownloadUrl().addOnSuccessListener(
                            url->   {
                                Glide.with(restaurantPic).load(url).into(restaurantPic);
                            }
                    );
                }
                if (restaurant.getCategory() != null) {
                    categoryTV.setText(restaurant.getCategory());
                }
                if (restaurant.getDescription() != null) {
                    descriptionTV.setText(restaurant.getDescription());
                }
                if (restaurant.getAddress() != null) {
                    addressTV.setText(restaurant.getAddress());
                    addressTV.setOnClickListener(
                            v -> {
                                Intent i = new Intent(this, RestaurantLocation.class);
                                i.putExtra("location",restaurant.getAddress());
                                startActivity(i);
                            }
                    );
                }
                if(restaurant.getMinPrice()!= null && restaurant.getMaxPrice()!=null){
                    String price = "Min $" + restaurant.getMinPrice() + " - Max $" + restaurant.getMaxPrice();
                    pricesTV.setText(price);
                }
                if(restaurant.getOpeningTime() != null && restaurant.getClosingTime() != null){
                    String sch = restaurant.getOpeningTime() + " - " + restaurant.getClosingTime();
                    scheduleTV.setText(sch);
                }
            }
        });

        //Check if restaurant is already in favorites
        FirebaseFirestore.getInstance().collection("users").document(currentClient.getId()).collection("favorites").addSnapshotListener(
                (value, error) -> {
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        String resId = (String) doc.get("resId");
                        if(resId.equals(restaurant.getId())){
                            added = true;
                            favBttn.setBackgroundResource(R.drawable.fav_fill);
                            break;
                        }
                    }
                }
        );
    }

    public void addToFavorites(View v){
        HashMap<String, String> object = new HashMap<>();
        String id = UUID.randomUUID().toString();
        object.put("id", id);
        object.put("resId", restaurant.getId());
        if(!added){
            //If restaurant is not in favorites, add it.
            db.collection("users").document(currentClient.getId()).collection("favorites").document(id).set(object);
            added = true;
            Toast.makeText(getApplicationContext(), "¡Agregado a favoritos!", Toast.LENGTH_SHORT).show();
            favBttn.setBackgroundResource(R.drawable.fav_fill);
        }else{
            favBttn.setBackgroundResource(R.drawable.fav_out);
            //If already is, delete from collection.
            Query query = FirebaseFirestore.getInstance().collection("users").document(currentClient.getId()).collection("favorites").whereEqualTo("resId", restaurant.getId());
            query.get().addOnCompleteListener(
                    task -> {
                        if (task.getResult().size() > 0) {
                            Restaurant restaurant = null;
                            for (DocumentSnapshot doc : task.getResult()) {
                                restaurant = doc.toObject(Restaurant.class);
                                doc.getReference().delete();
                                break;
                            }
                        }
                    }
            );
            added = false;
        }
    }

    public void loadMenu(View v){
        Intent i = new Intent(this, ClientMenuList.class);
        i.putExtra("restaurant", restaurant);
        startActivity(i);
    }

    public void goBackTo (View view) {
        finish();
    }
}