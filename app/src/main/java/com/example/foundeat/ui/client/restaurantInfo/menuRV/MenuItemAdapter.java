package com.example.foundeat.ui.client.restaurantInfo.menuRV;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foundeat.R;
import com.example.foundeat.model.MenuItem;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

//RecyclerView adapter (pieces together skeleton and model)
public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemView> {

    //List of model items.
    private ArrayList<MenuItem> menuItems;

    public MenuItemAdapter(){
        menuItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public MenuItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Creates row view from given layout XML
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.client_menu_item_row, parent, false);
        MenuItemView skeleton = new MenuItemView(row);

        return skeleton;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemView skeleton, int position) {
        //Get current item from model list
        MenuItem item = menuItems.get(position);
        //Bind model and view
        FirebaseStorage.getInstance().getReference().child("MenuItemsPhoto").child(item.getImage()).getDownloadUrl().addOnSuccessListener(
                url->   {
                    Glide.with(skeleton.getMenuItemRowIV()).load(url).into(skeleton.getMenuItemRowIV());
                }
        );
        skeleton.getMenuItemRowNameTV().setText(item.getName());
        skeleton.getMenuItemRowPriceTV().setText("$"+item.getPrice());
        skeleton.getMenuItemRowDescTV().setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public void addMenuItem(MenuItem item){
        menuItems.add(item);
        notifyItemInserted(menuItems.size()-1);
    }
}
