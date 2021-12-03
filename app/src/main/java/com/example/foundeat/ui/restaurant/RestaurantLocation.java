package com.example.foundeat.ui.restaurant;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

public class RestaurantLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private String location;

    private Marker marker;

    private ImageView goBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_restaurant_location);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void getLocation() {
        location = getIntent().getExtras().getString("location");

        Geocoder coder = new Geocoder(this);
        List<Address> addresses;
        LatLng p1 = null;
        try{
                addresses = coder.getFromLocationName(location, 5);
                if(addresses!=null){
                    Address l = addresses.get(0);
                   // Log.e(">>>>>>>>>","LOCATION DECODED: "+l.getLatitude()+", "+ l.getLongitude());

                    p1 = new LatLng(l.getLatitude(), l.getLongitude());
                    //Log.e(">>>>>>>","LATLONG: "+p1);
                    marker = mMap.addMarker(new MarkerOptions().position(p1));
                    marker.setTitle(location);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(p1,16));
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
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

        goBackBtn = findViewById(R.id.goBackBtn);
        goBackBtn.setOnClickListener(
                v->{
                    Intent intent = new Intent(this, RestaurantHome.class);
                    startActivity(intent);
                }
        );

        getLocation();

    }

}