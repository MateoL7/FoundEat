package com.example.foundeat.ui.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Review;
import com.example.foundeat.ui.restaurant.ReviewAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ClientReviews extends AppCompatActivity {

    private RecyclerView reviewRecycler;
    private TextView backTV;

    private ReviewAdapter adapter;
    private Client client;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_reviews);

        client = (Client) getIntent().getExtras().get("client");

        adapter = new ReviewAdapter();

        reviewRecycler = findViewById(R.id.favoritesListRecycler);
        backTV = findViewById(R.id.backTV);

        backTV.setOnClickListener(v -> {
            finish();
        });

        reviewRecycler.setHasFixedSize(true);
        reviewRecycler.setLayoutManager(new LinearLayoutManager(this));
        reviewRecycler.setAdapter(adapter);

        loadReviews();
    }

    private void loadReviews() {
        db.collection("reviews").whereEqualTo("customerID",client.getId()).addSnapshotListener(
                (value, error) -> {
                    adapter.getReviews().clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Review review = doc.toObject(Review.class);
                        adapter.getReviews().add(review);
                    }
                    adapter.notifyDataSetChanged();
                }
        );
    }
}