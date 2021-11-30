package com.example.foundeat.model;

import java.util.Date;

public class Review {

    private String id;
    private String customerName;
    private String content;
    private String customerPic;
    private Date date;

    public Review() {
    }

    public Review(String id, String customerName, String content, String customerPic, Date date) {
        this.id = id;
        this.customerName = customerName;
        this.content = content;
        this.customerPic = customerPic;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
