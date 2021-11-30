package com.example.foundeat.ui.restaurant;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.example.foundeat.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RestaurantPickLocation extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private Marker marker;
    private String dir;

    private ImageView goBackBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_restaurant_pick_location);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        goBackBtn = findViewById(R.id.goBackBtn);
        goBackBtn.setOnClickListener(
                v->{
                    //go back with the intent
                    Intent intent = new Intent();
                    intent.putExtra("location",dir);
                    setResult(RESULT_OK, intent);
                    finish();
                }
        );

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Marker p = mMap.addMarker(new MarkerOptions().position(latLng).title("Marcador").snippet("Pruebaaaa"));
        //points.add(p);

        if(marker==null){
            marker = mMap.addMarker(new MarkerOptions().position(latLng));
        }
        else{
            marker.setPosition(latLng);
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
        Geocoder g = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> ads = g.getFromLocation(latLng.latitude, latLng.longitude, 1);
            dir = ads.get(0).getAddressLine(0);
            marker.setTitle(dir);

            Log.e(">>>>>",dir);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}