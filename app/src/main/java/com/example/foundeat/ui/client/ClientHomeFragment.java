package com.example.foundeat.ui.client;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foundeat.R;
import com.example.foundeat.databinding.FragmentClientHomeBinding;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.FoodCategory;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.client.categoriesList.CategoriesListAdapter;

import com.example.foundeat.ui.client.restaurantList.ListsRestaurantsClients;

import com.example.foundeat.ui.client.favoritesList.FavoritesListAdapter;

import com.example.foundeat.ui.client.restaurantList.RestaurantListAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

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

    private RecyclerView favoritesRecycler;
    private LinearLayoutManager favoritesManager;
    private FavoritesListAdapter favoritesListAdapter;

    private ImageView restauranteRecomendadoImage;
    private TextView restauranteRecomendadoTV;
    private ImageView homeClientProfilePicIV;
    private ImageButton searchBtn;
    private EditText searchRestaurantET;

    private int cantidaMaximaReviews=0;
    private Restaurant restauranteRecomendado;

    private RecyclerView categoriesListRV;
    private LinearLayoutManager categoriesListRVManager;
    private CategoriesListAdapter categoriesListAdapter;


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

        favoritesRecycler = view.findViewById(R.id.favoritesListRecycler);
        favoritesManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        favoritesListAdapter = new FavoritesListAdapter();
        favoritesRecycler.setLayoutManager(favoritesManager);
        favoritesRecycler.setAdapter(favoritesListAdapter);
        favoritesRecycler.setHasFixedSize(true);

        categoriesListRV = view.findViewById(R.id.categoriesListRV);
        categoriesListRVManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        categoriesListAdapter = new CategoriesListAdapter();
        categoriesListRV.setLayoutManager(categoriesListRVManager);
        categoriesListRV.setAdapter(categoriesListAdapter);
        categoriesListRV.setHasFixedSize(true);

        restauranteRecomendadoImage= view.findViewById(R.id.restauranteRecomendadoImage);
        restauranteRecomendadoTV= view.findViewById(R.id.restauranteRecomendadoTV);
        homeClientProfilePicIV=view.findViewById(R.id.homeClientProfilePicIV);
        searchRestaurantET= view.findViewById(R.id.searchRestaurantET);
        searchBtn=view.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this::buscarRestaurante);

        loadFavorites();
        cargarDatosRstaurantes();
        cargarCategories();
        cargarFotoUsuario();
        return view;
    }


    private void buscarRestaurante(View view) {
        //TODO: Toast cuando no encuentra
        FirebaseFirestore.getInstance().collection("restaurants").whereEqualTo("name",searchRestaurantET.getText().toString()).get().addOnCompleteListener(
                task -> {
                    for (DocumentSnapshot doc:task.getResult()){
                        Restaurant newRestaurant = doc.toObject(Restaurant.class);
                        Intent intent = new Intent(view.getContext(), ListsRestaurantsClients.class);
                        intent.putExtra("restaurant",newRestaurant);
                        view.getContext().startActivity(intent);
                    }
                }
        );
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public void loadFavorites(){
        FirebaseFirestore.getInstance().collection("users").document(client.getId()).collection("favorites").addSnapshotListener(
                (value, error) -> {
                    favoritesListAdapter.getRestaurants().clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        String resId = (String) doc.get("resId");
                        bringRestaurant(resId);
                    }
                }
        );
    }

    private void bringRestaurant(String resId) {
        FirebaseFirestore.getInstance().collection("restaurants").document(resId).get().addOnSuccessListener(document -> {
            Restaurant restaurant = document.toObject(Restaurant.class);
            favoritesListAdapter.getRestaurants().add(restaurant);
            favoritesListAdapter.notifyDataSetChanged();
        });
    }

    public void cargarCategories(){
        FirebaseFirestore.getInstance().collection("food").get().addOnCompleteListener(
                task -> {
                    for (DocumentSnapshot doc:task.getResult()){
                        FoodCategory category = doc.toObject(FoodCategory.class);
                        categoriesListAdapter.addCategory(category);
                    }
                }
        );
    }

    public void cargarFotoUsuario(){
        if (client!=null&&client.getProfilePic()!=null&&!client.getProfilePic().equals("")){
            FirebaseStorage.getInstance().getReference().child("clientPhotos").child(client.getProfilePic()).getDownloadUrl().addOnSuccessListener(
                    url->   {
                        Glide.with(homeClientProfilePicIV).load(url).into(homeClientProfilePicIV);
                    }
            );
        }

    }

}