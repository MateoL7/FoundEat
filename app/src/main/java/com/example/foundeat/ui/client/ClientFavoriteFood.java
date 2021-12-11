package com.example.foundeat.ui.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.FoodCategory;
import com.example.foundeat.ui.client.categoriesGrid.CategoriesGridAdapter;
import com.example.foundeat.ui.client.categoriesList.CategoriesListAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ClientFavoriteFood extends AppCompatActivity implements CategoriesGridAdapter.AllCategoriesListener {

    private Client client;

    private GridLayoutManager manager;
    private CategoriesGridAdapter adapter;
    private RecyclerView categoryGrid;
    private Button continueBtn;
    private TextView skipTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_favorite_food);

        client = (Client) getIntent().getExtras().get("client");

        categoryGrid = findViewById(R.id.categoryGrid);
        continueBtn =  findViewById(R.id.continueBtn);
        skipTV = findViewById(R.id.skipTV);

        adapter = new CategoriesGridAdapter();
        adapter.setListener(this);
        manager = new GridLayoutManager(this,3,RecyclerView.HORIZONTAL,false);
        categoryGrid.setAdapter(adapter);
        categoryGrid.setLayoutManager(manager);
        categoryGrid.setHasFixedSize(true);

        continueBtn.setEnabled(false);
        continueBtn.setOnClickListener(this::nextActivity);

        loadCategories();

        skipTV.setOnClickListener(
                v->{
                    client.setFavoriteFood(null);
                    nextActivity(v);
                }
        );

    }

    public void loadCategories(){
        FirebaseFirestore.getInstance().collection("food").get().addOnCompleteListener(
                task -> {
                    for (DocumentSnapshot doc:task.getResult()){
                        FoodCategory category = doc.toObject(FoodCategory.class);
                        adapter.addCategory(category);
                    }
                }
        );
    }

    private void nextActivity(View view){
        FirebaseFirestore.getInstance().collection("users").document(client.getId()).set(client);
        client.setFavoriteFood(adapter.getFavCategories());
        Intent intent = new Intent(this,ClientHome.class);
        intent.putExtra("client", client);
        startActivity(intent);
    }

    @Override
    public void allCategoriesDone(ArrayList<String> categories) {
        continueBtn.setEnabled(true);
        client.setFavoriteFood(categories);
    }

    @Override
    public void allCategoriesNotDone() {
        continueBtn.setEnabled(false);
        client.setFavoriteFood(null);
    }
}