package com.example.foundeat.ui.client.categoriesList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foundeat.R;
import com.example.foundeat.model.FoodCategory;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.client.restaurantList.RestaurantListView;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class CategoriesListAdapter extends RecyclerView.Adapter<CategoriesListView> {

    private ArrayList<FoodCategory> categories;

    public CategoriesListAdapter(){
        categories = new ArrayList<>();
    }
    public void addCategory(FoodCategory category){
        categories.add(category);
        notifyItemInserted(categories.size()-1);
    }

    @NonNull
    @Override
    public CategoriesListView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row =inflater.inflate(R.layout.category_row,parent,false);
        CategoriesListView skeleton = new CategoriesListView(row);
        return skeleton;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesListView skeleton, int position) {
        FoodCategory category = categories.get(position);
            FirebaseStorage.getInstance().getReference().child("categoriesPhotos").child(category.getImagen()).getDownloadUrl().addOnSuccessListener(
                    url->   {
                        Glide.with(skeleton.getCategoryImage()).load(url).into(skeleton.getCategoryImage());
                    }
            );
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
