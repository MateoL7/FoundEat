package com.example.foundeat.model;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Restaurant implements Serializable {

    private String id,name,email,description, category, address,minPrice, maxPrice, accountInfo;
    private ArrayList<String> pics = new ArrayList<>();
    private String openingTime, closingTime;

    public Restaurant() {
    }

    public Restaurant(String id, String name, String email, String description, String category, String address, String minPrice, String maxPrice, String accountInfo, ArrayList<String> pics, String openingTime, String closingTime) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.description = description;
        this.category = category;
        this.address = address;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.accountInfo = accountInfo;
        this.pics = pics;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(String accountInfo) {
        this.accountInfo = accountInfo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public ArrayList<String> getPics() {
        return pics;
    }

    public void setPics(ArrayList<String> pics) {
        this.pics = pics;
    }
}
