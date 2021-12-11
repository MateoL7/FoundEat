package com.example.foundeat.ui.client.restaurantsFiltred;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.foundeat.R;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.client.restaurantList.RestaurantListAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RestaurantWithFiltersActivity extends AppCompatActivity {

    private RecyclerView restaurantFiltersRV;
    private RestaurantFilterAdapter restaurantFilterAdapter;

    private String maxPrice;
    private String minPrice;
    private String horaCierre;
    private ArrayList<String> categoriesSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_with_filters);

        restaurantFiltersRV = findViewById(R.id.restaurantListRV);
        restaurantFilterAdapter = new RestaurantFilterAdapter();
        restaurantFiltersRV.setLayoutManager(new LinearLayoutManager(this));
        restaurantFiltersRV.setAdapter(restaurantFilterAdapter);
        restaurantFiltersRV.setHasFixedSize(true);

        maxPrice=getIntent().getExtras().getString("maxPrice");
        minPrice=getIntent().getExtras().getString("minPrice");
        horaCierre =getIntent().getExtras().getString("horaCierre");
        categoriesSelected=(ArrayList<String>) getIntent().getExtras().get("categorias");
        cargarDatosRstaurantes();
    }

     public void cargarDatosRstaurantes(){
         Log.e("Aquiii: ", maxPrice +" - "+ minPrice+ " - ");
        FirebaseFirestore.getInstance().collection("restaurants").get().addOnCompleteListener(
                task -> {
                    for (DocumentSnapshot doc:task.getResult()){
                        Restaurant newRestaurant = doc.toObject(Restaurant.class);
                        restaurantFilterAdapter.addRestaurant(newRestaurant);
                    }
                }
        );
    }
}