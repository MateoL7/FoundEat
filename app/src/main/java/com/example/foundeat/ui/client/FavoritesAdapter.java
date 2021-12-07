package com.example.foundeat.ui.client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foundeat.R;
import com.example.foundeat.model.Restaurant;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesViewHolder> {

    private ArrayList<Restaurant> restaurants;

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
        if(restaurant.getPics().size()>0 && restaurant.getPics()!=null){
            Glide.with(holder.getPic().getContext()).load(restaurant.getPics().get(0)).centerCrop().into(holder.getPic());
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
