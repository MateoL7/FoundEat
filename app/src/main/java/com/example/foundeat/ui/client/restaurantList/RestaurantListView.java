package com.example.foundeat.ui.client.restaurantList;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.client.restaurantInfo.ClientRestaurantInfo;

public class RestaurantListView extends RecyclerView.ViewHolder {

    private Restaurant restaurant;
    private Client currentClient;

    private ImageView image;
    private TextView nombre, tipoComida,resenas,calificacion;

    public RestaurantListView(@NonNull View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.restaurantListImage);
        nombre = itemView.findViewById(R.id.restaurantListNombre);
        tipoComida = itemView.findViewById(R.id.restaurantListTipo);
        resenas = itemView.findViewById(R.id.restaurantListResenas);
        calificacion = itemView.findViewById(R.id.restaurantListCalificacion);

        image.setOnClickListener(this::mostrarRestaurante);
    }

    private void mostrarRestaurante(View view) {
        Intent intent = new Intent(view.getContext(), ClientRestaurantInfo.class);
        intent.putExtra("restaurant",restaurant);
        intent.putExtra("client", currentClient);
        view.getContext().startActivity(intent);

    }

    public void setCurrentClient(Client c){
        currentClient = c;
    }

    public Restaurant getRestaurantListModel() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public ImageView getImage() {
        return image;
    }

    public TextView getNombre() {
        return nombre;
    }

    public TextView getTipoComida() {
        return tipoComida;
    }

    public TextView getResenas() {
        return resenas;
    }

    public TextView getCalificacion() {
        return calificacion;
    }


}
