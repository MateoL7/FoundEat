package com.example.foundeat.ui.client.favoritesProfileList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.client.favoritesProfileList.FavoritesViewHolder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesViewHolder> {

    private ArrayList<Restaurant> restaurants;
    private Client client;

    public FavoritesAdapter() {
        restaurants = new ArrayList<>();
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.restaurant_row,parent,false);
        FavoritesViewHolder holder = new FavoritesViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.getCategory().setText(restaurant.getCategory());
        holder.getName().setText(restaurant.getName());
        holder.setRestaurant(restaurant);
        holder.setClient(client);
        if(restaurant.getPics().size()>0 && restaurant.getPics()!=null){
            FirebaseStorage.getInstance().getReference().child("restaurantPhotos").child(restaurant.getPics().get(0)).getDownloadUrl().addOnSuccessListener(
                    url->   {
                        Glide.with(holder.getPic()).load(url).into(holder.getPic());
                    }
            );
        }
        FirebaseFirestore.getInstance().collection("reviews").whereEqualTo("restaurantID",restaurant.getId()).addSnapshotListener(
                (value, error) -> {
                    int reviews = 0;
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        reviews++;
                    }
                    holder.getReviews().setText("("+reviews+" reseñas)");
                }
        );
        holder.getRating().setText(restaurant.getRating()+"");
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
