package com.example.foundeat.ui.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.restaurant.RestaurantLocation;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

import org.w3c.dom.Document;

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
    private ImageButton favBttn;

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

        //Add listeners to buttons
        favBttn.setOnClickListener(this::addToFavorites);

        //Get objects from intents
        restaurant=(Restaurant) getIntent().getExtras().get("restaurant");
        currentClient = (Client) getIntent().getExtras().get("client");

        //Load info into view
        loadRestaurantInfo();
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
                                //TODO Aleja, aquí va lo de cargar la ubicación

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
        query = db.collection("users").document(currentClient.getId()).collection("favoriteRestaurants");
        query.get().addOnCompleteListener(task ->{
            if(task.getResult().size() > 0){
                Restaurant temp;
                for(DocumentSnapshot doc : task.getResult()){
                    temp = doc.toObject(Restaurant.class);
                    if(temp.getId().equals(restaurant.getId())){
                        added = true;
                        break;
                    }
                }
            }
        });
        String json = new Gson().toJson(restaurant);
        getSharedPreferences("foundEat",MODE_PRIVATE).edit().putString("restaurant",json).apply();
    }

    public void addToFavorites(View v){
        if(!added){
            //If restaurant is not in favorites, add it.
            db.collection("users").document(currentClient.getId()).collection("favoriteRestaurants").document(restaurant.getId()).set(restaurant);
            added = true;
        }else{
            //If already is, delete from collection.
            db.collection("users").document(currentClient.getId()).collection("favoriteRestaurants").document(restaurant.getId()).delete();
            added = false;
        }
    }
}