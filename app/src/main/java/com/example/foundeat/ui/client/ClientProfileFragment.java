package com.example.foundeat.ui.client;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foundeat.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientProfileFragment extends Fragment {

    public ClientProfileFragment() {
        // Required empty public constructor
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
        return inflater.inflate(R.layout.fragment_client_profile, container, false);
    }
}