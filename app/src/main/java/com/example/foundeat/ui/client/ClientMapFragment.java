package com.example.foundeat.ui.client;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foundeat.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientMapFragment extends Fragment {

    public ClientMapFragment() {
        // Required empty public constructor
    }
    public static ClientMapFragment newInstance() {
        ClientMapFragment fragment = new ClientMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_map, container, false);
    }
}