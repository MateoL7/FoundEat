package com.example.foundeat.ui.restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.foundeat.R;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.model.Review;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RestaurantReviews extends AppCompatActivity {

    private RecyclerView reviewRecycler;
    private TextView backTV;

    private ReviewAdapter adapter;
    private Restaurant restaurant;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_reviews);

        restaurant = (Restaurant) getIntent().getExtras().get("restaurant");

        adapter = new ReviewAdapter();

        reviewRecycler = findViewById(R.id.reviewRecycler);
        backTV = findViewById(R.id.backTV);

        backTV.setOnClickListener(v->{
            finish();
        });

        reviewRecycler.setHasFixedSize(true);
        reviewRecycler.setLayoutManager(new LinearLayoutManager(this));
        reviewRecycler.setAdapter(adapter);

        loadReviews();

    }

    private void loadReviews() {
        db.collection("restaurants").document(restaurant.getId()).collection("reviews").addSnapshotListener(
                (value, error) -> {
                    adapter.getReviews().clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Review review = doc.toObject(Review.class);
                        adapter.getReviews().add(review);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}