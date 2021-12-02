package com.example.foundeat.ui.restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foundeat.R;
import com.example.foundeat.model.Restaurant;
import com.example.foundeat.ui.MainActivity;
import com.example.foundeat.ui.restaurant.menuList.MenuItemModel;
import com.example.foundeat.ui.restaurant.menuList.MenuListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

public class RestaurantHome extends AppCompatActivity {
    private Restaurant restaurant;

    private TextView nameET,categoryTV,descriptionTV,addressTV,scheduleTV,priceTV,menuTV, reviewsTV;
    private ImageView actualPic;
    private Button editProfileBtn;
    private NavigationView navigationView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_restaurant_home);

        restaurant = (Restaurant) getIntent().getExtras().get("restaurant");

        nameET = findViewById(R.id.nameTV);
        actualPic = findViewById(R.id.actualPic);
        loadRestaurantPhoto();
        categoryTV = findViewById(R.id.categoryTV);
        descriptionTV = findViewById(R.id.descriptionTV);
        addressTV = findViewById(R.id.addressTV);
        scheduleTV = findViewById(R.id.scheduleTV);
        priceTV = findViewById(R.id.priceTV);
        menuTV = findViewById(R.id.menuTV);
        reviewsTV = findViewById(R.id.reviewsTV);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.nav_logout){
                logout();
            }else if(item.getItemId() == R.id.nav_delete){
                delete();
            }
            return true;
        });

        loadProfileInfo();

        mayorMenor(restaurant.getId());

        editProfileBtn = findViewById(R.id.editProfileBtn);
        editProfileBtn.setOnClickListener(this::editProfile);
        menuTV.setOnClickListener(this::editMenu);
        reviewsTV.setOnClickListener(this::showReviews);
    }

    private void loadRestaurantPhoto(){
        if(restaurant.getPics().size()>0){
            FirebaseStorage.getInstance().getReference().child("restaurantPhotos").child(restaurant.getPics().get(0)).getDownloadUrl().addOnSuccessListener(
                    url->   {
                        Glide.with(actualPic).load(url).into(actualPic);
                    }
            );
        }
    }

    private void showReviews(View view) {
        Intent intent = new Intent(this,RestaurantReviews.class);
        intent.putExtra("restaurant",restaurant);
        startActivity(intent);
    }

    private void logout() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        getSharedPreferences("foundEat", MODE_PRIVATE).edit().clear().apply();
        FirebaseAuth.getInstance().signOut();
    }
    private void delete(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        String password = "123456";
        user.updatePassword(password);
        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider.getCredential(email,password);

        // Prompt the user to re-provide their sign-in credentials
        if (user != null) {
            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> user.delete()
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(RestaurantHome.this, "Lamentamos que te vayas.\nTu restaurante ha sido eliminado de la base de datos.", Toast.LENGTH_LONG).show();
                                    Query query = db.collection("restaurants").whereEqualTo("id",restaurant.getId());
                                    query.get().addOnCompleteListener(task2->{
                                        if(task2.getResult().size() > 0){
                                            Restaurant res = null;
                                            for(DocumentSnapshot doc : task2.getResult()){
                                                res = doc.toObject(Restaurant.class);
                                                doc.getReference().delete();
                                                break;
                                            }
                                        }
                                    });
                                    logout();
                                }
                            }));
        }
    }

    private void loadProfileInfo() {
        Query query =  db.collection("restaurants").whereEqualTo("id",restaurant.getId());
        query.get().addOnCompleteListener(task->{
            if (task.getResult().size() > 0) {
                for (DocumentSnapshot doc : task.getResult()) {
                    restaurant = doc.toObject(Restaurant.class);
                    break;
                }
                nameET.setText("Perfil\n" + restaurant.getName());

                if (restaurant.getPics() != null && restaurant.getPics().size() > 0) {
//                    Log.e(">>>>>>", "HOLAAAAA >>> " + restaurant.getPics().get(0));
//                    Bitmap bitmap = BitmapFactory.decodeFile(restaurant.getPics().get(0));
//                    Bitmap thumbnail = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 4, bitmap.getHeight() / 4, true);
//                    actualPic.setImageBitmap(thumbnail);
                    FirebaseStorage.getInstance().getReference().child("restaurantPhotos").child(restaurant.getPics().get(0)).getDownloadUrl().addOnSuccessListener(
                            url->   {
                                Glide.with(actualPic).load(url).into(actualPic);
                            }
                    );
                }
                if (restaurant.getCategory() != null) {
                    categoryTV.setText(restaurant.getCategory());
                }
                if (restaurant.getDescription() != null) {
                    descriptionTV.setText(restaurant.getDescription());
                }
                if (restaurant.getAddress() != null) {
                    addressTV.setText(restaurant.getAddress());
                }
                if (restaurant.getOpeningTime() != null && restaurant.getClosingTime() != null) {

                }
                if(restaurant.getMinPrice()!= null && restaurant.getMaxPrice()!=null){
                    String price = "Min $" + restaurant.getMinPrice() + " - Max $" + restaurant.getMaxPrice();
                    priceTV.setText(price);
                }
            }
        });
        saveRestaurant(restaurant);
    }

    private void editProfile(View view){
        Intent intent = new Intent(this, RestaurantEditProfile.class);
        intent.putExtra("restaurant",restaurant);
        startActivity(intent);
    }


    private void saveRestaurant(Restaurant restaurant){
        String json = new Gson().toJson(restaurant);
        getSharedPreferences("foundEat",MODE_PRIVATE).edit().putString("restaurant",json).apply();
    }

    private void editMenu(View view){
        Intent intent = new Intent(this, MenuListActivity.class);
        intent.putExtra("restaurant", restaurant);
        startActivity(intent);
    }

    public  void mayorMenor(String name){

        Query query = db.collection("restaurants").document(name).collection("menu");
        query.get().addOnCompleteListener(
                task -> {
                    int mayor=0;
                    int menor=Integer.MAX_VALUE;
                    MenuItemModel s = null;

                    for(DocumentSnapshot ds :task.getResult() ) {

                        s=ds.toObject(MenuItemModel.class);

                        int temp = Integer.parseInt(s.getPrice());
                        if(temp>mayor && temp>menor){
                            mayor = temp;
                        }
                        else if(temp<menor ){
                            menor = temp;
                        }

                    }
                    priceTV.setText("Máx $"+mayor+"- Min $"+menor+"");

                }
        );
    }
}