package com.example.foundeat.ui.restaurant.menuList;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foundeat.R;
import com.example.foundeat.model.MenuItem;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Map;

//RecyclerView adapter (pieces together skeleton and model)
public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemView> {

    //List of model items.
    private ArrayList<MenuItem> menuItems;

    private ActivityResultLauncher<Intent> launcher;

    public MenuItemAdapter(){
        menuItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public MenuItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Creates row view from given layout XML
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.menu_item_row, parent, false);
        MenuItemView skeleton = new MenuItemView(row);

        return skeleton;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemView skeleton, int position) {
        //Get current item from model list
        MenuItem item = menuItems.get(position);
        skeleton.setItem(item);
        skeleton.setLauncher(launcher);

        //Bind model and view

        //TODO implement image display
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

    public void clear(){
        menuItems.clear();
        notifyDataSetChanged();
    }

    public ArrayList<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setLauncher(ActivityResultLauncher<Intent> launcher) {
        this.launcher = launcher;
    }
}
