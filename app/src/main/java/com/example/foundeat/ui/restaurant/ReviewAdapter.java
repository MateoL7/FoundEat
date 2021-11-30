package com.example.foundeat.ui.restaurant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foundeat.R;
import com.example.foundeat.model.Review;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    private ArrayList<Review> reviews;

    public ReviewAdapter(){
        reviews = new ArrayList<>();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.reviewrow,parent,false);
        ReviewViewHolder holder = new ReviewViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.getContentTV().setText(review.getContent());
        holder.getCustomerNameTV().setText(review.getCustomerName());

        if(review.getCustomerPic()!=null){
            Glide.with(holder.getCustomerPic().getContext()).load(review.getCustomerPic()).centerCrop().into(holder.getCustomerPic());
        }

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
