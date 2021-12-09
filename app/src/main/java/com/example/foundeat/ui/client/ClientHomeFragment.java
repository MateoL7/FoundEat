package com.example.foundeat.ui.client;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foundeat.R;
import com.example.foundeat.databinding.FragmentClientHomeBinding;
import com.example.foundeat.model.Client;
import com.example.foundeat.ui.client.restaurantList.RestaurantListAdapter;
import com.example.foundeat.ui.client.restaurantList.RestaurantListModel;
import com.example.foundeat.ui.restaurant.menuList.MenuItemModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
        cargarDatosRstaurantes();
        return view;
    }


    public void cargarDatosRstaurantes(){
        FirebaseFirestore.getInstance().collection("restaurants").get().addOnCompleteListener(
                task -> {
                    for (DocumentSnapshot doc:task.getResult()){
                        RestaurantListModel newRestaurant = doc.toObject(RestaurantListModel.class);
                        restaurantListAdapter.addRestaurant(newRestaurant);
                    }
                }
        );
    }

}