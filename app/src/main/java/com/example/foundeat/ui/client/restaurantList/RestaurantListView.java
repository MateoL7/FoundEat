package com.example.foundeat.ui.client.restaurantList;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foundeat.R;
import com.example.foundeat.ui.restaurant.menuList.AddMenuItemActivity;

public class RestaurantListView extends RecyclerView.ViewHolder {

    private RestaurantListModel restaurantListModel;

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

        Intent intent = new Intent(view.getContext(), ListsRestaurantsClients.class);
        intent.putExtra("restaurant",restaurantListModel);
        view.getContext().startActivity(intent);

    }

    public RestaurantListModel getRestaurantListModel() {
        return restaurantListModel;
    }

    public void setRestaurantListModel(RestaurantListModel restaurantListModel) {
        this.restaurantListModel = restaurantListModel;
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
