package com.example.foundeat.ui.client;

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
import com.example.foundeat.ui.restaurant.ReviewAdapter;
import com.example.foundeat.util.HTTPSWebUtilDomi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        client = (Client) getIntent().getExtras().get("client");
        restaurant = (Restaurant) getIntent().getExtras().get("restaurant");
        contentReview = findViewById(R.id.contentReview);
        addClientReviewBtn = findViewById(R.id.addClientReviewBtn);

        goBackTV = findViewById(R.id.goBackTV);


        goBackTV.setOnClickListener(v -> {
            finish();
        });

        addClientReviewBtn.setOnClickListener(this::addReview);

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
            Review review = new Review( id,  customerID,  restaurantID,  customerPic,  customerName,  restaurantName,  content,  date);

            //Manda la notificacion de la review
            new Thread(
                    () -> {
                        FCMMessage<Review> fcmMessage = new FCMMessage<>("/topics/"+restaurant.getId(), review);
                        String json = new Gson().toJson(fcmMessage);
                        new HTTPSWebUtilDomi().POSTtoFCM(json);
                    }
            ).start();

            FirebaseFirestore.getInstance().collection("reviews").document(review.getId()).set(review).addOnSuccessListener(task->{
                Toast.makeText(this, "¡Gracias por tu opinión!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }
}
