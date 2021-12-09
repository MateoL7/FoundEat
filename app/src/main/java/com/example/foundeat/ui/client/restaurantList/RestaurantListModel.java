package com.example.foundeat.ui.client.restaurantList;

import java.io.Serializable;
import java.util.ArrayList;

public class RestaurantListModel implements Serializable {

    private String id;



    private String address, category, closingTime, description, email,maxPrice,minPrice,name,openingTime;
    private ArrayList<String> pics;
    public RestaurantListModel() {
    }

    public RestaurantListModel(String id, String address, String category, String closingTime, String description, String email, String maxPrice, String minPrice, String name, String openingTime, ArrayList<String> pics) {
        this.id = id;
        this.address = address;
        this.category = category;
        this.closingTime = closingTime;
        this.description = description;
        this.email = email;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.name = name;
        this.openingTime = openingTime;
        this.pics = pics;
    }

    public ArrayList<String> getPics() {
        return pics;
    }

    public void setPics(ArrayList<String> pics) {
        this.pics = pics;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }
}
