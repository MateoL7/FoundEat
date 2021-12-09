package com.example.foundeat.model;

import java.util.Date;

public class Review {

    private String id;
    private String customerID;
    private String restaurantID;
    private String customerPic;
    private String customerName;
    private String restaurantName;
    private String content;
    private Date date;

    public Review() {
    }

    public Review(String id, String customerID, String restaurantID, String customerPic, String customerName, String restaurantName, String content, Date date) {
        this.id = id;
        this.customerID = customerID;
        this.restaurantID = restaurantID;
        this.customerPic = customerPic;
        this.customerName = customerName;
        this.restaurantName = restaurantName;
        this.content = content;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCustomerPic() {
        return customerPic;
    }

    public void setCustomerPic(String customerPic) {
        this.customerPic = customerPic;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
