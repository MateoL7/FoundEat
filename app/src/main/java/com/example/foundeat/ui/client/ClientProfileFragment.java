package com.example.foundeat.ui.client;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;

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

        favoritesTV = view.findViewById(R.id.favoritesTV);
        myReviewsTV = view.findViewById(R.id.myReviewsTV);
        editProfileTV = view.findViewById(R.id.editProfileTV);
        settingsBtn = view.findViewById(R.id.settingsBtn);
        pp = view.findViewById(R.id.pp);
        userTV = view.findViewById(R.id.userTV);

        userTV.setText(client.getName()+"\n"+client.getEmail());

        editProfileTV.setOnClickListener(this::editProfile);

        return view;
    }
    public void showFavorites(){

    }
    public void showReviews(){

    }
    public void editProfile(View view){
        Intent intent = new Intent(home,ClientEditProfile.class);
        intent.putExtra("client",client);
        startActivity(intent);
    }

}