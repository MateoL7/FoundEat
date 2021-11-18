package com.example.foundeat.model;

import java.io.Serializable;

public class Restaurant implements Serializable {

    private String id,name,email,description, address,minPrice, maxPrice;
    private Long openingTime, closingTime;

    public Restaurant() {
    }

    public Restaurant(String id, String name, String email, String description, String address, String minPrice, String maxPrice, Long openingTime, Long closingTime) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.description = description;
        this.address = address;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
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

    public Long getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Long openingTime) {
        this.openingTime = openingTime;
    }

    public Long getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Long closingTime) {
        this.closingTime = closingTime;
    }
}
