package com.example.foundeat.ui.client.restaurantList;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.ui.restaurant.ReviewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ClientAddReview extends AppCompatActivity {


    private RecyclerView reviewRecycler;
    private ImageButton goBackTV;

    private ReviewAdapter adapter;
    private Client client;
    private FirebaseUser actualUser;
    private Button addClientReviewBtn;
    private EditText contentReview;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        client = (Client) getIntent().getExtras().get("client");
        actualUser = FirebaseAuth.getInstance().getCurrentUser();
        contentReview=findViewById(R.id.contentReview);
        addClientReviewBtn=findViewById(R.id.addClientReviewBtn);

        goBackTV = findViewById(R.id.goBackTV);


        goBackTV.setOnClickListener(v->{
            finish();
        });

        addClientReviewBtn.setOnClickListener(this::addReview);

    }

    private void addReview(View view) {


    }
}
