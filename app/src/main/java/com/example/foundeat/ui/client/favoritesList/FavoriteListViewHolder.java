package com.example.foundeat.ui.client.favoritesList;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foundeat.R;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.client.restaurantInfo.ClientRestaurantInfo;

public class FavoriteListViewHolder extends RecyclerView.ViewHolder {

    private ImageView resPic;
    private Restaurant restaurant;

    public FavoriteListViewHolder(@NonNull View itemView) {
        super(itemView);

        resPic = itemView.findViewById(R.id.resPic);
        resPic.setOnClickListener(this::showRestaurant);
    }
    public ImageView getResPic() {
        return resPic;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    private void showRestaurant(View view) {
        Intent intent = new Intent(view.getContext(), ClientRestaurantInfo.class);
        intent.putExtra("restaurant", restaurant);
        view.getContext().startActivity(intent);
    }
}
