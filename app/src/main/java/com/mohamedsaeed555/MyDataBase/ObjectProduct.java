package com.mohamedsaeed555.MyDataBase;

public class ObjectProduct {
    private String barcode;
    private int amount;

    public ObjectProduct(String barcode, int amount) {
        this.barcode = barcode;
        this.amount = amount;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
