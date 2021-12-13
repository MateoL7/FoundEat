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
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.client.restaurantList.RestaurantListAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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

    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_with_filters);

        client = (Client) getIntent().getExtras().get("client");

        restaurantesfiltrosTV=findViewById(R.id.restaurantesfiltrosTV);
        restaurantFiltersRV = findViewById(R.id.restaurantFiltersRV);
        restaurantFilterAdapter = new RestaurantListAdapter();
        restaurantFilterAdapter.addClient(client);
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
         Query q = FirebaseFirestore.getInstance().collection("restaurants");
         if(!categoriesSelected.isEmpty()){
             q = q.whereIn("category",categoriesSelected);
         }
         q.get().addOnCompleteListener(task -> {
                for(DocumentSnapshot doc: task.getResult()){
                    Restaurant rest = doc.toObject(Restaurant.class);
                    boolean aux = true;
                    if(rest.getMaxPrice() != null){
                        int minRest = Integer.parseInt(rest.getMinPrice());
                        if(maxPrice.equals("")){
                            maxPrice = "0";
                        }
                        int givenMax = Integer.parseInt(maxPrice);
                        if(minRest>givenMax){
                            aux = false;
                        }
                    }
                    if(rest.getMinPrice() != null){
                        int maxRest = Integer.parseInt(rest.getMaxPrice());
                        if(minPrice.equals("")){
                            minPrice = "0";
                        }
                        int givenMin = Integer.parseInt(minPrice);
                        if(givenMin>maxRest){
                            aux = false;
                        }
                    }
                    if(rest.getOpeningTime() != null && !horaInicio.equals("")){
                        if(rest.getOpeningTime().compareTo(horaInicio) > 0){
                            aux = false;
                        }
                    }
                    if(rest.getClosingTime() != null && !horaCierre.equals("")){
                        if(rest.getClosingTime().compareTo(horaCierre) < 0){
                            aux = false;
                        }
                    }
                    if(aux){
                        restaurantFilterAdapter.addRestaurant(rest);
                    }
                }
            }
         );
    }

}