package com.example.foundeat.model;


public class FoodCategory {

    private String id;
    private String category;
    private String imagen;

    public FoodCategory() {
    }

    public FoodCategory(String id, String category) {
        this.id = id;
        this.category = category;
    }

    public FoodCategory(String id, String category, String imagen) {
        this.id = id;
        this.category = category;
        this.imagen = imagen;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return category;
    }
}
