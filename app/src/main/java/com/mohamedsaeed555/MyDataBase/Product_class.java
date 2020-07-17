package com.mohamedsaeed555.MyDataBase;

public class Product_class {

    private String date;
    private int amount;
    private String _id;
    private String barcode;
    private String name;
    private double price;
    private String brand;
    private String image;
    private String collection;


    public Product_class(String date, int amount, String barcode, String name, double price, String brand, String image, String collection) {
        this.date = date;
        this.amount = amount;
        this.barcode = barcode;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.image = image;
        this.collection = collection;
    }

    public Product_class(String date, int amount, String barcode, String name, double price, String brand, String image) {
        this.date = date;
        this.amount = amount;
        this.barcode = barcode;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.image = image;
    }

    public Product_class(String barcode, String name, double price, String image) {
        this.barcode = barcode;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCollection() {
        return collection;
    }
}
