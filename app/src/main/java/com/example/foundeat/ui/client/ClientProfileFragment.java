package com.example.foundeat.ui.client;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.MainActivity;
import com.example.foundeat.ui.restaurant.RestaurantHome;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientProfileFragment extends Fragment {

    private TextView favoritesTV, myReviewsTV, editProfileTV, userTV;
    private ImageButton settingsBtn;
    private ImageView pp;

    private ClientHome home;
    private Client client;



    public ClientProfileFragment() {

    }

    public static ClientProfileFragment newInstance() {
        ClientProfileFragment fragment = new ClientProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_profile, container, false);
        home = (ClientHome) getActivity();
        client = home.getClient();
        loadPhoto();

        favoritesTV = view.findViewById(R.id.favoritesTV);
        myReviewsTV = view.findViewById(R.id.myReviewsTV);
        editProfileTV = view.findViewById(R.id.editProfileTV);
        settingsBtn = view.findViewById(R.id.settingsBtn);
        pp = view.findViewById(R.id.pp);
        userTV = view.findViewById(R.id.userTV);
        settingsBtn = view.findViewById(R.id.settingsBtn);

        userTV.setText(client.getName()+"\n"+client.getEmail());

        editProfileTV.setOnClickListener(this::editProfile);
        favoritesTV.setOnClickListener(this::showFavorites);
        myReviewsTV.setOnClickListener(this::showReviews);


        return view;
    }

    private void loadPhoto() {
        if(client.getProfilePic()!=null){
            FirebaseStorage.getInstance().getReference().child("clientPhotos").child(client.getProfilePic()).getDownloadUrl().addOnSuccessListener(
                    url->   {
                        Glide.with(pp).load(url).into(pp);
                    }
            );
        }
    }

    public void showFavorites(View view){
        Intent intent = new Intent(getActivity(),ClientFavorites.class);
        intent.putExtra("client",client);
        startActivity(intent);
    }
    public void showReviews(View view){
        Intent intent = new Intent(getActivity(),ClientReviews.class);
        intent.putExtra("client",client);
        startActivity(intent);
    }
    public void editProfile(View view){
        Intent intent = new Intent(home,ClientEditProfile.class);
        intent.putExtra("client",client);
        startActivity(intent);

    }

}