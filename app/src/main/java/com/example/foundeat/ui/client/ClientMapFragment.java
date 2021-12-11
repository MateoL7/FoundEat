package com.example.foundeat.ui.client;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.model.Review;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientMapFragment extends Fragment {

    private ArrayList<String> resIds;

    private Client client;

    private ArrayList<Restaurant> restaurants;

    private GoogleMap mMap;

    private Marker marker;

    public ClientMapFragment() {
        // Required empty public constructor
    }
    public static ClientMapFragment newInstance() {
        ClientMapFragment fragment = new ClientMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;


            restaurants = new ArrayList<Restaurant>();

            loadFavorites();

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);

        }
    }

    public void setRestaurants(ArrayList<String> res){
        resIds = res;

    }

    public void loadMap(Restaurant r){
        Geocoder coder = new Geocoder(getView().getContext());
        List<Address> addresses;
        LatLng p1 = null;
        try{
                addresses = coder.getFromLocationName(r.getAddress(), 5);
                if(addresses!=null){
                    Address location = addresses.get(0);
                    Log.e(">>>>>>>>>","LOCATION DECODED: "+location.getLatitude()+", "+ location.getLongitude());

                    p1 = new LatLng(location.getLatitude(), location.getLongitude());
                    // Log.e(">>>>>>>","LATLONG: "+p1);
                    marker = mMap.addMarker(new MarkerOptions().position(p1));
                    marker.setTitle(r.getName());
                    marker.setSnippet(r.getAddress());
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFavorites(){
        if (client!=null){
        FirebaseFirestore.getInstance().collection("users").document(client.getId()).collection("favorites").addSnapshotListener(
                    (value, error) -> {
                        if(value.getDocuments().isEmpty()){
                            getAllRestaurants();
                        }
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            String resId = (String) doc.get("resId");
                            Log.e(">>>>>",resId);
                            bringRestaurant(resId);
                        }
                    }
            );
        }
    }

    private void getAllRestaurants() {
        FirebaseFirestore.getInstance().collection("restaurants").get().addOnCompleteListener(
                task -> {
                    for (DocumentSnapshot doc:task.getResult()){
                        Restaurant restaurant = doc.toObject(Restaurant.class);
                        Log.e(">>>>",restaurant.getName());
                        restaurants.add(restaurant);
                        loadMap(restaurant);
                    }
                }
        );
    }

    private void bringRestaurant(String resId) {
        FirebaseFirestore.getInstance().collection("restaurants").document(resId).get().addOnSuccessListener(document -> {
            Restaurant restaurant = document.toObject(Restaurant.class);
            Log.e(">>>>",restaurant.getName());
            restaurants.add(restaurant);
            loadMap(restaurant);
        });
    }

    public void setClient(Client client) {
        this.client = client;
    }
}