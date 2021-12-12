package com.example.foundeat.ui.client.favoritesList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoriteListViewHolder>{

    private ArrayList<Restaurant> restaurants;
    private Client client;

    public FavoritesListAdapter() {
        restaurants = new ArrayList<>();
    }

    public void setClient(Client c){client = c;}

    @NonNull
    @Override
    public FavoriteListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.favorite_row,parent,false);
        FavoriteListViewHolder holder = new FavoriteListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteListViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.setRestaurant(restaurant);
        holder.setClient(client);
        if(restaurant.getPics()!=null && restaurant.getPics().size()>0){
            FirebaseStorage.getInstance().getReference().child("restaurantPhotos").child(restaurant.getPics().get(0)).getDownloadUrl().addOnSuccessListener(
                    url->   {
                        Glide.with(holder.getResPic()).load(url).into(holder.getResPic());
                    }
            );
        }
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }
    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }
}
