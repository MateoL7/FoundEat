package com.example.foundeat.ui.client.restaurantInfo.menuRV;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foundeat.R;

public class MenuItemView extends RecyclerView.ViewHolder {
    private ImageView menuItemRowIV;
    private TextView menuItemRowNameTV, menuItemRowPriceTV, menuItemRowDescTV;

    public MenuItemView(@NonNull View itemView) {
        super(itemView);
        menuItemRowIV = itemView.findViewById(R.id.menuItemRowIV);
        menuItemRowNameTV = itemView.findViewById(R.id.menuItemRowNameTV);
        menuItemRowPriceTV = itemView.findViewById(R.id.menuItemRowPriceTV);
        menuItemRowDescTV = itemView.findViewById(R.id.menuItemRowDescTV);
    }
    public ImageView getMenuItemRowIV() {
        return menuItemRowIV;
    }

    public TextView getMenuItemRowNameTV() {
        return menuItemRowNameTV;
    }

    public TextView getMenuItemRowPriceTV() {
        return menuItemRowPriceTV;
    }

    public TextView getMenuItemRowDescTV() {
        return menuItemRowDescTV;
    }
}
