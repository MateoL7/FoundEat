package com.example.foundeat.ui.client;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foundeat.R;
import com.example.foundeat.databinding.FragmentClientHomeBinding;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.client.restaurantList.RestaurantListAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientHomeFragment extends Fragment {


    private Client client;
    private FragmentClientHomeBinding binding;
    private RecyclerView restaurantListRV;
    private LinearLayoutManager restaurantListRVManager;
    private RestaurantListAdapter restaurantListAdapter;
    private ImageView restauranteRecomendadoImage;
    private TextView restauranteRecomendadoTV;

    private int cantidaMaximaReviews=0;
    private Restaurant restauranteRecomendado;


    public ClientHomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ClientHomeFragment newInstance() {
        ClientHomeFragment fragment = new ClientHomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentClientHomeBinding.inflate(inflater,container, false);
        View view = binding.getRoot();
        restaurantListRV = view.findViewById(R.id.restaurantListRV);
        restaurantListRVManager = new LinearLayoutManager(getActivity());
        restaurantListAdapter = new RestaurantListAdapter();
        restaurantListRV.setLayoutManager(restaurantListRVManager);
        restaurantListRV.setAdapter(restaurantListAdapter);
        restaurantListRV.setHasFixedSize(true);
        restauranteRecomendadoImage= view.findViewById(R.id.restauranteRecomendadoImage);
        restauranteRecomendadoTV= view.findViewById(R.id.restauranteRecomendadoTV);

        cargarDatosRstaurantes();

        return view;
    }



    synchronized  public void calcularReviewsMaximas(Restaurant restaurantLocal){
        FirebaseFirestore.getInstance().collection("reviews").whereEqualTo("restaurantID",restaurantLocal.getId()).get().addOnCompleteListener(
                task -> {
                    int cantidadReviwew=0;
                    for (DocumentSnapshot doc:task.getResult()){
                        cantidadReviwew++;
                    }
                    if (cantidadReviwew>=cantidaMaximaReviews){
                        cantidaMaximaReviews=cantidadReviwew;
                        restauranteRecomendado= restaurantLocal;
                    }
                    cargarRestauranteFavorito();
                }
        );
    }

    synchronized public void cargarRestauranteFavorito(){
        if (restauranteRecomendado!=null&&!(restauranteRecomendado.getPics().isEmpty())){
            FirebaseStorage.getInstance().getReference().child("restaurantPhotos").child(restauranteRecomendado.getPics().get(0)).getDownloadUrl().addOnSuccessListener(
                    url->   {
                        Glide.with(restauranteRecomendadoImage).load(url).into(restauranteRecomendadoImage);
                        restauranteRecomendadoTV.setText(restauranteRecomendado.getName());
                    }
            );
        }
    }

    synchronized public void cargarDatosRstaurantes(){
        FirebaseFirestore.getInstance().collection("restaurants").get().addOnCompleteListener(
                task -> {
                    for (DocumentSnapshot doc:task.getResult()){
                        Restaurant newRestaurant = doc.toObject(Restaurant.class);
                        restaurantListAdapter.addRestaurant(newRestaurant);
                        calcularReviewsMaximas(newRestaurant);

                    }
                }
        );
    }

}