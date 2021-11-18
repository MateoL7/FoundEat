package com.example.foundeat.model;

import java.io.Serializable;

public class Client implements Serializable {

    private String id, name, email,profilePic;
    private String[] favoriteFood;

    public Client() {
    }

    public Client(String id, String name, String email, String profilePic, String[] favoriteFood) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profilePic = profilePic;
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

    public String[] getFavoriteFood() {
        return favoriteFood;
    }

    public void setFavoriteFood(String[] favoriteFood) {
        this.favoriteFood = favoriteFood;
    }
}
