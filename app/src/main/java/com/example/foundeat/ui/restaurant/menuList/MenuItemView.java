package com.example.foundeat.ui.restaurant.menuList;

import android.content.Intent;
import android.media.Image;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foundeat.R;
import com.example.foundeat.model.MenuItem;

//Line inside a RecyclerView
public class MenuItemView extends RecyclerView.ViewHolder {

    private ImageView menuItemRowIV;
    private TextView menuItemRowNameTV, menuItemRowPriceTV, menuItemRowDescTV;
    private ImageButton editMenuItemRowBtn;
    private MenuItem menuItem;

    private ActivityResultLauncher<Intent> launcher;

    public MenuItemView(@NonNull View itemView) {
        super(itemView);
        menuItemRowIV = itemView.findViewById(R.id.menuItemRowIV);
        menuItemRowNameTV = itemView.findViewById(R.id.menuItemRowNameTV);
        menuItemRowPriceTV = itemView.findViewById(R.id.menuItemRowPriceTV);
        menuItemRowDescTV = itemView.findViewById(R.id.menuItemRowDescTV);
        editMenuItemRowBtn = itemView.findViewById(R.id.editMenuItemRowBtn);

        editMenuItemRowBtn.setOnClickListener(this::editMenuItem);
    }

    private void editMenuItem(View view) {
        Intent intent = new Intent(view.getContext(), AddMenuItemActivity.class);
        intent.putExtra("editing", true);
        intent.putExtra("menuItem",menuItem);
        launcher.launch(intent);
    }



    public void setItem(MenuItem men){
        menuItem = men;
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

    public ImageButton getEditMenuItemRowBtn() {
        return editMenuItemRowBtn;
    }

    public void setLauncher(ActivityResultLauncher<Intent> launcher) {
        this.launcher = launcher;
    }
}
