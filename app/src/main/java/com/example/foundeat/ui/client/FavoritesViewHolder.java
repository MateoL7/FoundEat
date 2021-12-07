package com.example.foundeat.ui.client;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foundeat.R;

public class FavoritesViewHolder extends RecyclerView.ViewHolder {

    private ImageView pic;
    private TextView name, category, reviews;
    private Button favBtn;

    public FavoritesViewHolder(@NonNull View itemView) {
        super(itemView);

        pic = itemView.findViewById(R.id.pic);
        name = itemView.findViewById(R.id.name);
        category = itemView.findViewById(R.id.category);
        reviews = itemView.findViewById(R.id.reviews);
        favBtn = itemView.findViewById(R.id.favBtn);
    }

    public ImageView getPic() {
        return pic;
    }

    public TextView getName() {
        return name;
    }

    public TextView getCategory() {
        return category;
    }

    public TextView getReviews() {
        return reviews;
    }

    public Button getFavBtn() {
        return favBtn;
    }
}
