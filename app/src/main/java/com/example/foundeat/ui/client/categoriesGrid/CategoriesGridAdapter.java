package com.example.foundeat.ui.client.categoriesGrid;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.model.FoodCategory;
import com.example.foundeat.ui.client.categoriesList.CategoriesListView;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class CategoriesGridAdapter extends RecyclerView.Adapter<CategoriesGridView> {

    private ArrayList<FoodCategory> categories;
    private ArrayList<String> favCategories;

    private AllCategoriesListener listener;

    public CategoriesGridAdapter() {
        categories = new ArrayList<>();
        favCategories = new ArrayList<>();
    }

    public void addCategory(FoodCategory category) {
        categories.add(category);
        notifyItemInserted(categories.size() - 1);
    }

    @NonNull
    @Override
    public CategoriesGridView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.category_row, parent, false);
        CategoriesGridView holder = new CategoriesGridView(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesGridView skeleton, int position) {
        FoodCategory category = categories.get(position);
        FirebaseStorage.getInstance().getReference().child("categoriesPhotos").child(category.getImagen()).getDownloadUrl().addOnSuccessListener(
                url -> {
                    Glide.with(skeleton.getCategoryImage()).load(url).into(skeleton.getCategoryImage());
                }
        );
        skeleton.getCategoryImage().setOnClickListener(v->{
            if(skeleton.getCategoryImage().getAlpha()==1){
                if(favCategories.size()>=3){
                    Toast.makeText(v.getContext(), "Ya tienes 3 categorias seleccionadas",Toast.LENGTH_LONG).show();
                    listener.allCategoriesDone(favCategories);
                }else{
                    favCategories.add(category.getCategory());
                    skeleton.getCategoryImage().setAlpha((float) 0.2);
                }
            }else if(skeleton.getCategoryImage().getAlpha()<1){
                removeCategory(category.getCategory());
                listener.allCategoriesNotDone();
                skeleton.getCategoryImage().setAlpha((float) 1);
            }
        });
    }

    private void removeCategory(String category) {
        for(int i = 0; i<favCategories.size();i++){
            if(favCategories.get(i).equalsIgnoreCase(category)){
                favCategories.remove(favCategories.get(i));
                break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return categories.size();
    }

    public interface AllCategoriesListener{
        void allCategoriesDone(ArrayList<String> categories);
        void allCategoriesNotDone();
    }

    public ArrayList<FoodCategory> getCategories() {
        return categories;
    }

    public ArrayList<String> getFavCategories() {
        return favCategories;
    }

    public void setListener(AllCategoriesListener listener) {
        this.listener = listener;
    }
}
