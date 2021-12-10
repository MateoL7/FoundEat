package com.example.foundeat.ui.client.restaurantList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foundeat.R;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.restaurant.menuList.MenuItemModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.UUID;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListView> {

    private ArrayList<Restaurant> restaurants;

    public RestaurantListAdapter(){
        restaurants = new ArrayList<>();
    }

    public void addRestaurant(Restaurant restaurant){
        restaurants.add(restaurant);
        notifyItemInserted(restaurants.size()-1);
    }

    @NonNull
    @Override
    public RestaurantListView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row =inflater.inflate(R.layout.cliente_restaurant_list_row,parent,false);
        RestaurantListView skeleton = new RestaurantListView(row);
        return skeleton;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantListView skeleton, int position) {
        Restaurant restaurant = restaurants.get(position);
        if (!restaurant.getPics().isEmpty()){
        FirebaseStorage.getInstance().getReference().child("restaurantPhotos").child(restaurant.getPics().get(0)).getDownloadUrl().addOnSuccessListener(
                url->   {
                    Glide.with(skeleton.getImage()).load(url).into(skeleton.getImage());
                }
        );
        }

        FirebaseFirestore.getInstance().collection("reviews").whereEqualTo("restaurantID",restaurant.getId()).get().addOnCompleteListener(
                task -> {
                    int cantidadReviwew=0;
                    for (DocumentSnapshot doc:task.getResult()){
                        cantidadReviwew++;
                    }
                    skeleton.getResenas().setText("Cantidad de reseñas: "+cantidadReviwew);
                }
        );

        skeleton.setRestaurant(restaurant);
        skeleton.getCalificacion().setText("CALIFICACION NO ESTA EN LA BD");
        skeleton.getNombre().setText(restaurant.getName());
        skeleton.getTipoComida().setText(restaurant.getCategory());
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}
