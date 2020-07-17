package com.mohamedsaeed555.MyDataBase;

public class DetailsProductOrder {
    private Product_class product;
    private int amount;

    public DetailsProductOrder(Product_class product, int amount) {
        this.product = product;
        this.amount = amount;
    }

    public Product_class getProduct() {
        return product;
    }

    public void setProduct(Product_class product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
