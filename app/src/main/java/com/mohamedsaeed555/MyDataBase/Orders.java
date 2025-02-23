package com.mohamedsaeed555.MyDataBase;

import java.util.ArrayList;

public class Orders {
    private Users by;
    private ArrayList<ObjectProduct> products;
    private ArrayList<Double> total;
    private Boolean delivery;
    private String deliveryAt;
    private Boolean paid;
    private String paidAt;
    private String _id;
    private String created_at;

    public Orders(Users by, ArrayList<ObjectProduct> products, ArrayList<Double> total, Boolean delivery, String deliveryAt, Boolean paid, String paidAt, String _id, String created_at) {
        this.by = by;
        this.products = products;
        this.total = total;
        this.delivery = delivery;
        this.deliveryAt = deliveryAt;
        this.paid = paid;
        this.paidAt = paidAt;
        this._id = _id;
        this.created_at = created_at;
    }

    public Orders(Users by, ArrayList<ObjectProduct> products) {
        this.by = by;
        this.products = products;
    }


    public Orders(Boolean delivery, String deliveryAt , String created_at) {
        this.delivery = delivery;
        this.deliveryAt = deliveryAt;
    }

    public Orders(Boolean paid, String paidAt) {
        this.paid = paid;
        this.paidAt = paidAt;
    }

    public String getCreated_at() {
        return created_at;
    }

    public ArrayList<Double> getTotal() {
        return total;
    }

    public void setTotal(ArrayList<Double> total) {
        this.total = total;
    }

    public Users getBy() {
        return by;
    }

    public void setBy(Users by) {
        this.by = by;
    }

    public ArrayList<ObjectProduct> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ObjectProduct> products) {
        this.products = products;
    }

    public Boolean getDelivery() {
        return delivery;
    }

    public void setDelivery(Boolean delivery) {
        this.delivery = delivery;
    }

    public String getDeliveryAt() {
        return deliveryAt;
    }

    public void setDeliveryAt(String deliveryAt) {
        this.deliveryAt = deliveryAt;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public String getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(String paidAt) {
        this.paidAt = paidAt;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

}

