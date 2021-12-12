package com.example.foundeat.ui.client.favoritesProfileList;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.client.restaurantInfo.ClientRestaurantInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FavoritesViewHolder extends RecyclerView.ViewHolder {

    private ImageView pic;
    private TextView name, category, reviews;
    private Button favBtn;

    private Restaurant restaurant;
    private Client client;

    public FavoritesViewHolder(@NonNull View itemView) {
        super(itemView);

        pic = itemView.findViewById(R.id.pic);
        name = itemView.findViewById(R.id.name);
        category = itemView.findViewById(R.id.category);
        reviews = itemView.findViewById(R.id.reviews);
        favBtn = itemView.findViewById(R.id.favBtn);

        pic.setOnClickListener(this::showRestaurant);
        favBtn.setOnClickListener(this::unFavorite);
    }

    private void showRestaurant(View view) {
        Intent intent = new Intent(view.getContext(), ClientRestaurantInfo.class);
        intent.putExtra("restaurant", restaurant);
        intent.putExtra("client", client);
        view.getContext().startActivity(intent);
    }

    private void unFavorite(View view) {
        Query query = FirebaseFirestore.getInstance().collection("users").document(client.getId()).collection("favorites").whereEqualTo("resId", restaurant.getId());
        query.get().addOnCompleteListener(
                task -> {
                    if (task.getResult().size() > 0) {
                        Restaurant restaurant = null;
                        for (DocumentSnapshot doc : task.getResult()) {
                            restaurant = doc.toObject(Restaurant.class);
                            doc.getReference().delete();
                            break;
                        }
                    }
                }
        );
    }

    public ImageView getPic() {
        return pic;
    }

    public TextView getName() {
        return name;
    }

    public TextView getCategory() {
        return category;
    }

    public TextView getReviews() {
        return reviews;
    }

    public Button getFavBtn() {
        return favBtn;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
