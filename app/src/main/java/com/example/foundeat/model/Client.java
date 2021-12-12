package com.example.foundeat.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Client implements Serializable {

    private String id, name, email,profilePic;
    private String accountInfo;
    private ArrayList<String> favoriteFood;

    public Client() {
    }

    public Client(String id, String name, String email, String profilePic, String accountInfo, ArrayList<String> favoriteFood) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profilePic = profilePic;
        this.accountInfo = accountInfo;
        this.favoriteFood = favoriteFood;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(String accountInfo) {
        this.accountInfo = accountInfo;
    }

    public ArrayList<String> getFavoriteFood() {
        return favoriteFood;
    }

    public void setFavoriteFood(ArrayList<String> favoriteFood) {
        this.favoriteFood = favoriteFood;
    }
}
