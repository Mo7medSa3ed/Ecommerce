package com.mohamedsaeed555.ecommerce;

import com.mohamedsaeed555.MyDataBase.Product_class;

import java.util.ArrayList;

public class One_product_class{

    private Product_class product;
    private ArrayList<Product_class> samilier;

    public Product_class getProduct() {
        return product;
    }

    public void setProduct(Product_class product) {
        this.product = product;
    }

    public ArrayList<Product_class> getSamilier() {
        return samilier;
    }

    public void setSamilier(ArrayList<Product_class> samilier) {
        this.samilier = samilier;
    }
}