package com.example.foundeat.ui.client;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.MainActivity;
import com.example.foundeat.ui.client.restaurantList.RestaurantListAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ClientHome extends AppCompatActivity {

    private Client client;

    private BottomNavigationView navigator;
    private ClientHomeFragment clientHomeFragment;
    private ClientMapFragment clientMapFragment;
    private ClientProfileFragment clientProfileFragment;


    private RecyclerView restaurantListRV;
    private LinearLayoutManager restaurantListRVManager;
    private RestaurantListAdapter restaurantListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        client = (Client) getIntent().getExtras().get("client");
        loadClient();

        navigator = findViewById(R.id.navigator);
        clientHomeFragment = ClientHomeFragment.newInstance();

        clientMapFragment = ClientMapFragment.newInstance();


        clientProfileFragment = ClientProfileFragment.newInstance();


        showFragment(clientHomeFragment);
        clientHomeFragment.setClient(client);
        navigator.setOnItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.homeItem) {
                showFragment(clientHomeFragment);
            } else if (menuItem.getItemId() == R.id.profileItem) {
                showFragment(clientProfileFragment);
            } else if (menuItem.getItemId() == R.id.mapItem) {
                clientMapFragment.setClient(client);
                showFragment(clientMapFragment);
            }
            return true;
        });



    }

    public void showFragment(Fragment f) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, f);
        transaction.commit();
    }

    private void loadClient() {
        //Sacar todo de la base de datos
        Query query = FirebaseFirestore.getInstance().collection("users").whereEqualTo("id", client.getId());
        query.get().addOnCompleteListener(task -> {
            if (task.getResult().size() > 0) {
                for (DocumentSnapshot doc : task.getResult()) {
                    client = doc.toObject(Client.class);
                    break;
                }
            }
        });
        //Guardar el cliente
        saveClient(client);
    }

    private void logout(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        getSharedPreferences("foundEat", MODE_PRIVATE).edit().clear().apply();
        FirebaseAuth.getInstance().signOut();
    }

    //TODO:Revisar con FB
    private void saveClient(Client client) {
        Log.e(">>>",client.getName());
        String json = new Gson().toJson(client);
        getSharedPreferences("foundEat", MODE_PRIVATE).edit().putString("client", json).apply();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }



}