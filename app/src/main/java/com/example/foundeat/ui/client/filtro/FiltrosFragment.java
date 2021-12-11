package com.example.foundeat.ui.client.filtro;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.foundeat.R;
import com.example.foundeat.model.FoodCategory;
import com.example.foundeat.model.Restaurant;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FiltrosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FiltrosFragment extends DialogFragment implements FiltrosView.OnCategorySelected{


    private RecyclerView filtroRV;
    private GridLayoutManager filtrosManager;
    private FiltrosAdapter filtrosAdapter;

    private TextView horaCierreET,minPriceET,maxPriceET,horaInicioET;
    private Button aplicarFiltrosBtn;
    
    private int maxPrice=0;
    private int minPrice=2147483645;

    private ArrayList<String> categoriesSelected;

    public FiltrosFragment() {
        // Required empty public constructor
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
        horaInicioET = view.findViewById(R.id.horaInicioET);
        minPriceET = view.findViewById(R.id.minPriceET);
        minPriceET.setText(""+0);
        maxPriceET = view.findViewById(R.id.maxPriceET);
        aplicarFiltrosBtn = view.findViewById(R.id.aplicarFiltrosBtn);
        aplicarFiltrosBtn.setOnClickListener(this::enviarFiltrosHome);
        horaCierreET.setOnClickListener(this::pickTime);
        horaInicioET.setOnClickListener(this::pickTime);
        loadMaxPrice();
        loadMinPrice();
        //TODO: se puede cargar las hora minima y maxima desde los restaurantes de la base de datos
        //horaCierreET.setText();
        //horaInicioET.setText();
        return view;
    }

    public boolean valuesPriceIsOk(){
        if (Integer.parseInt(minPriceET.getText().toString())>Integer.parseInt(maxPriceET.getText().toString())){
            return false;
        }else{
            return true;
        }
    }

//  TODO: para esta verificacion debemos considerar los dias

//    public boolean valuesTimeIsOk(){
//        if (LocalTime.parse(horaInicioET.getText().toString()).isAfter(LocalTime.parse(horaCierreET.getText().toString()))){
//            return false;
//        }else{
//            return true;
//        }
//    }

    private void enviarFiltrosHome(View view) {
        if (valuesPriceIsOk()){
            Intent intent = new Intent(getContext(), RestaurantWithFiltersActivity.class);
            intent.putExtra("maxPrice",maxPriceET.getText().toString());
            intent.putExtra("minPrice",minPriceET.getText().toString());
            intent.putExtra("horaCierre",horaCierreET.getText().toString());
            intent.putExtra("horaInicio",horaInicioET.getText().toString());
            intent.putExtra("categorias", categoriesSelected);
            startActivity(intent);
        }else{
            Toast.makeText(getContext(),"El precio minimo es mayor al precio maximo o la hora de inicio es despues a la de cierre",Toast.LENGTH_LONG).show();
        }
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

    private void loadMinPrice(){
        FirebaseFirestore.getInstance().collection("restaurants").get().addOnCompleteListener(
                task -> {
                    for (DocumentSnapshot doc:task.getResult()){
                        Restaurant newRestaurant = doc.toObject(Restaurant.class);
                        if (newRestaurant.getMinPrice()!=null&&!newRestaurant.getMinPrice().equals(""))
                            if(Integer.parseInt(newRestaurant.getMinPrice())<minPrice){
                                minPrice=Integer.parseInt(newRestaurant.getMinPrice());
                            }
                    }
                    minPriceET.setText(""+minPrice);
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
                switch(v.getId()){
                    case R.id.horaCierreET:
                        String hora = String.format("%02d", selectedHour);
                        String minuto = String.format("%02d", selectedMinute);
                        horaCierreET.setText( hora + ":" + minuto);
                        break;
                    case R.id.horaInicioET:
                        String hora2 = String.format("%02d", selectedHour);
                        String minuto2 = String.format("%02d", selectedMinute);
                        horaInicioET.setText( hora2 + ":" + minuto2);
                        break;
                }
                }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @Override
    public void onDataResult(String categoryFiltros) {
        if (!categoriesSelected.contains(categoryFiltros))
            categoriesSelected.add(categoryFiltros);
    }

}