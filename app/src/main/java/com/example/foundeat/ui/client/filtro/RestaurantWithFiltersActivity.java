package com.example.foundeat.ui.client.filtro;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.foundeat.R;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.client.restaurantList.RestaurantListAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class RestaurantWithFiltersActivity extends AppCompatActivity {

    private RecyclerView restaurantFiltersRV;
    private RestaurantListAdapter restaurantFilterAdapter;

    private String maxPrice;
    private String minPrice;
    private String horaCierre;
    private String horaInicio;
    private TextView restaurantesfiltrosTV;
    private ArrayList<String> categoriesSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_with_filters);

        restaurantesfiltrosTV=findViewById(R.id.restaurantesfiltrosTV);
        restaurantFiltersRV = findViewById(R.id.restaurantFiltersRV);
        restaurantFilterAdapter = new RestaurantListAdapter();
        restaurantFiltersRV.setLayoutManager(new LinearLayoutManager(this));
        restaurantFiltersRV.setAdapter(restaurantFilterAdapter);
        restaurantFiltersRV.setHasFixedSize(true);

        maxPrice=getIntent().getExtras().getString("maxPrice");
        minPrice=getIntent().getExtras().getString("minPrice");
        horaCierre =getIntent().getExtras().getString("horaCierre");
        horaInicio =getIntent().getExtras().getString("horaInicio");
        categoriesSelected=(ArrayList<String>) getIntent().getExtras().get("categorias");
        cargarDatosRstaurantes();
    }


     public void cargarDatosRstaurantes(){
         for (String category:categoriesSelected) {
             Log.e("Aquiii: ", maxPrice +" - "+ minPrice+ " - "+ horaCierre+ " - "+category);
             FirebaseFirestore.getInstance().collection("restaurants").whereEqualTo("category",category).get().addOnCompleteListener(
                     task -> {
                         if (task.getResult().isEmpty()){
                             restaurantesfiltrosTV.setText("Sin resultados");
                         }else{
                             restaurantesfiltrosTV.setText("Restaurantes obtenidos");
                         }
                         for (DocumentSnapshot doc:task.getResult()){
                             Restaurant newRestaurant = doc.toObject(Restaurant.class);
                             if (!(Integer.parseInt(maxPrice)<Integer.parseInt(newRestaurant.getMinPrice()))
                             &&!(Integer.parseInt(minPrice)>Integer.parseInt(newRestaurant.getMaxPrice()))){
                                 if (newRestaurant.getClosingTime()==null&&newRestaurant.getOpeningTime()==null){
                                     restaurantFilterAdapter.addRestaurant(newRestaurant);
                                 }else{
                                     //TODO: Revisar lógica del tiempo
                                     if (!LocalTime.parse(horaInicio).isAfter(LocalTime.parse(newRestaurant.getClosingTime()))
                                             &&!LocalTime.parse(horaCierre).isBefore(LocalTime.parse(newRestaurant.getOpeningTime()))){
                                         restaurantFilterAdapter.addRestaurant(newRestaurant);
                                     }
                                 }
                             }
                         }

                     }
             );
         }
         if (categoriesSelected.isEmpty()){
             FirebaseFirestore.getInstance().collection("restaurants").get().addOnCompleteListener(
                     task -> {
                         if (task.getResult().isEmpty()){
                             restaurantesfiltrosTV.setText("Sin resultados");
                         }else{
                             restaurantesfiltrosTV.setText("Restaurantes obtenidos");
                         }
                         for (DocumentSnapshot doc:task.getResult()){
                             Restaurant newRestaurant = doc.toObject(Restaurant.class);
                             if (!(Integer.parseInt(maxPrice)<Integer.parseInt(newRestaurant.getMinPrice()))
                                     &&!(Integer.parseInt(minPrice)>Integer.parseInt(newRestaurant.getMaxPrice()))){

                                 if (newRestaurant.getClosingTime()==null&&newRestaurant.getOpeningTime()==null){
                                     restaurantFilterAdapter.addRestaurant(newRestaurant);
                                 }else{
                                     if (!LocalTime.parse(horaInicio).isAfter(LocalTime.parse(newRestaurant.getClosingTime()))
                                             &&!LocalTime.parse(horaCierre).isBefore(LocalTime.parse(newRestaurant.getOpeningTime()))){
                                         restaurantFilterAdapter.addRestaurant(newRestaurant);
                                     }
                                 }
                             }
                         }

                     }
             );
         }
    }

}