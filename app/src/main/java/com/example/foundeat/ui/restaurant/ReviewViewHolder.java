package com.example.foundeat.ui.restaurant;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foundeat.R;

public class ReviewViewHolder extends RecyclerView.ViewHolder {

    private ImageView customerPic;
    private TextView customerNameTV, contentTV, ratingC;


    public ReviewViewHolder(@NonNull View itemView) {
        super(itemView);

        customerNameTV = itemView.findViewById(R.id.customerNameTV);
        contentTV = itemView.findViewById(R.id.contentTV);
        customerPic = itemView.findViewById(R.id.customerPic);
        ratingC = itemView.findViewById(R.id.ratingC);

    }

    public ImageView getCustomerPic() {
        return customerPic;
    }

    public TextView getCustomerNameTV() {
        return customerNameTV;
    }

    public TextView getContentTV() {
        return contentTV;
    }

    public TextView getRatingC() {
        return ratingC;
    }
}
