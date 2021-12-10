package com.example.foundeat.ui.client.categoriesList;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foundeat.R;

public class CategoriesListView extends RecyclerView.ViewHolder {

    private ImageView categoryImage;

    public CategoriesListView(@NonNull View itemView) {
        super(itemView);
        categoryImage = itemView.findViewById(R.id.categoryImage);
    }

    public ImageView getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(ImageView categoryImage) {
        this.categoryImage = categoryImage;
    }
}
