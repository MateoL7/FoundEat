package com.example.foundeat.ui.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foundeat.R;
import com.example.foundeat.fcm.FCMMessage;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.model.Review;
import com.example.foundeat.ui.client.restaurantInfo.ClientRestaurantInfo;
import com.example.foundeat.ui.restaurant.RestaurantMoreInfo;
import com.example.foundeat.ui.restaurant.ReviewAdapter;
import com.example.foundeat.util.HTTPSWebUtilDomi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.Date;
import java.util.UUID;

public class ClientAddReview extends AppCompatActivity {


    private ImageButton goBackTV;
    private Client client;
    private Restaurant restaurant;
    private Button addClientReviewBtn;
    private EditText contentReview;
    private Button star1,star2,star3,star4,star5;
    private int rating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        rating = 0;
        client = (Client) getIntent().getExtras().get("client");
        restaurant = (Restaurant) getIntent().getExtras().get("restaurant");
        contentReview = findViewById(R.id.contentReview);
        addClientReviewBtn = findViewById(R.id.addClientReviewBtn);
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        goBackTV = findViewById(R.id.goBackTV);


        goBackTV.setOnClickListener(v -> {
            finish();
        });


        addClientReviewBtn.setOnClickListener(this::addReview);

        star1.setOnClickListener(v->{
            rating = 1;
            //Prendidas
            star1.setBackground(null);
            //Apagadas
            star2.setBackground(null);
            star3.setBackground(null);
            star4.setBackground(null);
            star5.setBackground(null);

        });
        star2.setOnClickListener(v->{
            rating = 2;
            //Prendidas
            star1.setBackground(null);
            star2.setBackground(null);
            //Apagadas
            star3.setBackground(null);
            star4.setBackground(null);
            star5.setBackground(null);
        });
        star3.setOnClickListener(v->{
            rating = 3;
            //Prendidas
            star1.setBackground(null);
            star2.setBackground(null);
            star3.setBackground(null);
            //Apagadas
            star4.setBackground(null);
            star5.setBackground(null);
        });
        star4.setOnClickListener(v->{
            rating = 4;
            //Prendidas
            star1.setBackground(null);
            star2.setBackground(null);
            star3.setBackground(null);
            star4.setBackground(null);
            //Apagadas
            star5.setBackground(null);
        });
        star5.setOnClickListener(v->{
            rating = 5;
            //Prendidas
            star1.setBackground(null);
            star2.setBackground(null);
            star3.setBackground(null);
            star4.setBackground(null);
            star5.setBackground(null);
        });

    }

    private void addReview(View view) {
        if (contentReview.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor llena el campo de la reseña, tu opinión es importante para nosotros", Toast.LENGTH_SHORT).show();
        } else {
            String id = UUID.randomUUID().toString();
            String customerID = client.getId();
            String restaurantID = restaurant.getId();
            String customerPic = client.getProfilePic();
            String customerName = client.getName();
            String restaurantName = restaurant.getName();
            String content = contentReview.getText().toString();
            Date date = new Date();
            if(rating==0){
                Toast.makeText(this, "Califica al restaurante por favor", Toast.LENGTH_SHORT).show();
            }else{
                FirebaseFirestore.getInstance().collection("reviews").whereEqualTo("restaurantID",restaurant.getId()).get().addOnCompleteListener(
                        task -> {
                            int numReviews=0;
                            int ratingSum = 0;
                            for (DocumentSnapshot doc:task.getResult()){
                                int actualRating = (Integer) doc.get("rating");
                                numReviews++;
                                ratingSum = ratingSum + actualRating;
                            }
                            restaurant.setRating(ratingSum/numReviews);
                            FirebaseFirestore.getInstance().collection("restaurants").document(restaurant.getId()).set(restaurant);
                        }
                );
                Review review = new Review( id,  customerID,  restaurantID,  customerPic,  customerName,  restaurantName,  content, rating,  date);
                FirebaseFirestore.getInstance().collection("reviews").document(review.getId()).set(review).addOnSuccessListener(task->{
                    Toast.makeText(this, "¡Gracias por tu opinión!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, ClientRestaurantInfo.class);
                    intent.putExtra("client",client);
                    intent.putExtra("restaurant",restaurant);
                    startActivity(intent);
                });
                new Thread(
                        () -> {
                            FCMMessage<Review> fcmMessage = new FCMMessage<>("/topics/"+restaurant.getId(), review);
                            String json = new Gson().toJson(fcmMessage);
                            new HTTPSWebUtilDomi().POSTtoFCM(json);
                        }
                ).start();
            }
        }
    }
}
