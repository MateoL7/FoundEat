package com.example.foundeat.ui.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.client.favoritesProfileList.FavoritesAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ClientFavorites extends AppCompatActivity {

    private Client client;
    private RecyclerView favoritesRecycler;
    private TextView backTV;
    private FavoritesAdapter adapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_favorites);

        client = (Client) getIntent().getExtras().get("client");
        favoritesRecycler = findViewById(R.id.favoritesListRecycler);
        backTV = findViewById(R.id.backTV);
        adapter = new FavoritesAdapter();
        adapter.setClient(client);

        backTV.setOnClickListener(v -> {
            finish();
        });

        favoritesRecycler.setHasFixedSize(true);
        favoritesRecycler.setLayoutManager(new LinearLayoutManager(this));
        favoritesRecycler.setAdapter(adapter);

        loadFavorites();
    }

    private void loadFavorites() {
        db.collection("users").document(client.getId()).collection("favorites").addSnapshotListener(
                (value, error) -> {
                    adapter.getRestaurants().clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        String resId = (String) doc.get("resId");
                        bringRestaurant(resId);
                    }
                }
        );
    }

    private void bringRestaurant(String resId) {
        FirebaseFirestore.getInstance().collection("restaurants").document(resId).get().addOnSuccessListener(document -> {
            Restaurant restaurant = document.toObject(Restaurant.class);
            if(restaurant!=null){
                adapter.getRestaurants().add(restaurant);
                adapter.notifyDataSetChanged();
            }else{
                Query query = FirebaseFirestore.getInstance().collection("users").document(client.getId()).collection("favorites").whereEqualTo("resId",resId);
                query.get().addOnCompleteListener(task2->{
                    if(task2.getResult().size() > 0){
                        Restaurant res = null;
                        for(DocumentSnapshot doc : task2.getResult()){
                            doc.getReference().delete();
                            break;
                        }
                    }
                });
            }
        });
    }
}