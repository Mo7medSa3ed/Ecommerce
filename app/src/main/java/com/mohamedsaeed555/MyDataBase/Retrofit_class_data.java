package com.mohamedsaeed555.MyDataBase;

import java.util.ArrayList;

public class Retrofit_class_data {

    private ArrayList<Product_class> products = null;
    private String current;
    private int pages;

    public ArrayList<Product_class> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product_class> products) {
        this.products = products;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
