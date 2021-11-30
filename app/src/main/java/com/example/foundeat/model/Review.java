package com.example.foundeat.model;

import java.util.Date;

public class Review {

    private String id;
    private String customerID;
    private String customerPic;
    private String customerName;
    private String content;
    private Date date;

    public Review() {
    }

    public Review(String id, String customerID, String customerPic, String customerName, String content, Date date) {
        this.id = id;
        this.customerID = customerID;
        this.customerPic = customerPic;
        this.customerName = customerName;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
