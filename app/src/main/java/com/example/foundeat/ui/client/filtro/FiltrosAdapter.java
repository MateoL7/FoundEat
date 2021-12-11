package com.example.foundeat.ui.client.filtro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foundeat.R;
import com.example.foundeat.model.FoodCategory;
import com.example.foundeat.ui.client.categoriesList.CategoriesListView;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class FiltrosAdapter extends RecyclerView.Adapter<FiltrosView> {

    private ArrayList<FoodCategory> categorias;
    private FiltrosView.OnCategorySelected listener;

    public FiltrosAdapter(){
        categorias= new ArrayList<>();
    }

    public void addCategory(FoodCategory category){
        categorias.add(category);
        notifyItemInserted(categorias.size()-1);
    }
    public void setListener(FiltrosView.OnCategorySelected listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FiltrosView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row =inflater.inflate(R.layout.filtro_row,parent,false);
        FiltrosView skeleton = new FiltrosView(row);
        skeleton.setListener(listener);
        return skeleton;
    }

    @Override
    public void onBindViewHolder(@NonNull FiltrosView skeleton, int position) {
        FoodCategory category = categorias.get(position);
        skeleton.getCategoryFiltros().setText(category.getCategory());
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }
}
