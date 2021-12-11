package com.example.foundeat.ui.client.filtro;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.foundeat.R;
import com.example.foundeat.model.FoodCategory;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.client.restaurantsFiltred.RestaurantWithFiltersActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FiltrosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FiltrosFragment extends DialogFragment implements FiltrosView.OnCategorySelected{

    private OnFiltrosFragment listener;

    private RecyclerView filtroRV;
    private GridLayoutManager filtrosManager;
    private FiltrosAdapter filtrosAdapter;

    private TextView horaCierreET,minPriceET,maxPriceET;
    private Button aplicarFiltrosBtn;
    
    private int maxPrice=0;

    private ArrayList<String> categoriesSelected;

    public FiltrosFragment() {
        // Required empty public constructor
    }

    public void setListener(OnFiltrosFragment listener) {
        this.listener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    // TODO: Rename and change types and number of parameters
    public static FiltrosFragment newInstance() {
        FiltrosFragment fragment = new FiltrosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_filtros, container, false);
        categoriesSelected = new ArrayList<>();
        filtroRV = view.findViewById(R.id.filtroRV);
        filtrosManager= new GridLayoutManager(view.getContext(), 3,RecyclerView.HORIZONTAL,false);
        filtrosAdapter = new FiltrosAdapter();
        filtrosAdapter.setListener(this);
        filtroRV.setAdapter(filtrosAdapter);
        filtroRV.setLayoutManager(filtrosManager);
        filtroRV.setHasFixedSize(true);
        loadCategories();

        horaCierreET = view.findViewById(R.id.horaCierreET);
        minPriceET = view.findViewById(R.id.minPriceET);
        minPriceET.setText(""+0);
        maxPriceET = view.findViewById(R.id.maxPriceET);
        aplicarFiltrosBtn = view.findViewById(R.id.aplicarFiltrosBtn);
        aplicarFiltrosBtn.setOnClickListener(this::enviarFiltrosHome);
        horaCierreET.setOnClickListener(this::pickTime);
        loadMaxPrice();
        return view;
    }

    private void enviarFiltrosHome(View view) {
        listener.onDataResult(maxPriceET.getText().toString(),minPriceET.getText().toString(),horaCierreET.getText().toString(),categoriesSelected);
//        Intent intent = new Intent(getContext(), RestaurantWithFiltersActivity.class);
//        intent.putExtra("maxPrice",maxPriceET.getText().toString());
//        intent.putExtra("minPrice",minPriceET.getText().toString());
//        intent.putExtra("horaCierre",horaCierreET.getText().toString());
//        intent.putExtra("categorias", categoriesSelected);
//        startActivity(intent);
    }

    public void loadCategories(){
        FirebaseFirestore.getInstance().collection("food").get().addOnCompleteListener(
                task -> {
                    for (DocumentSnapshot doc:task.getResult()){
                        FoodCategory category = doc.toObject(FoodCategory.class);
                        filtrosAdapter.addCategory(category);
                    }
                }
        );
    }

    private void loadMaxPrice(){
        FirebaseFirestore.getInstance().collection("restaurants").get().addOnCompleteListener(
                task -> {
                    for (DocumentSnapshot doc:task.getResult()){
                        Restaurant newRestaurant = doc.toObject(Restaurant.class);
                        if (newRestaurant.getMaxPrice()!=null&&!newRestaurant.getMaxPrice().equals(""))
                        if(Integer.parseInt(newRestaurant.getMaxPrice())>maxPrice){
                            maxPrice=Integer.parseInt(newRestaurant.getMaxPrice());
                        }
                    }
                    maxPriceET.setText(""+maxPrice);
                }
        );
    }

    private void pickTime(View v){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                horaCierreET.setText( selectedHour + ":" + selectedMinute);
                }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @Override
    public void onDataResult(String categoryFiltros) {
        categoriesSelected.add(categoryFiltros);
        Log.e("Aquiii: ", categoriesSelected.get(0));
    }

    public interface  OnFiltrosFragment{
        public void onDataResult(String maxPrice,String minPrice,String horaCierre,ArrayList<String> categories);
    }
}