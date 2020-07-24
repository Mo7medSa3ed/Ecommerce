package com.mohamedsaeed555.Notification;

import com.mohamedsaeed555.MyDataBase.Orders;
import com.mohamedsaeed555.MyDataBase.Poset_Orders;
import com.mohamedsaeed555.MyDataBase.Product_class;

public class Notification_Class {
    private String Sender_id;
    private Boolean Admin;
    private String msg;
    private String Go_Activity;
    private String Collection;
    private Product_class product_class;
    private Orders orders;
    private Poset_Orders ord;

    public Notification_Class(Boolean admin, String msg, String go_Activity, String collection, Product_class product_class) {
        Admin = admin;
        this.msg = msg;
        Go_Activity = go_Activity;
        Collection = collection;
        this.product_class = product_class;
    }

    public Notification_Class(Boolean admin, String msg, String go_Activity, Orders orders) {
        Admin = admin;
        this.msg = msg;
        Go_Activity = go_Activity;
        this.orders = orders;
    }


    public Notification_Class(Boolean admin, String msg, String go_Activity, Poset_Orders ord) {
        Admin = admin;
        this.msg = msg;
        Go_Activity = go_Activity;
        this.ord = ord;
    }

    public Notification_Class(Boolean admin, String msg, String go_Activity) {
        Admin = admin;
        this.msg = msg;
        Go_Activity = go_Activity;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getAdmin() {
        return Admin;
    }

    public void setAdmin(Boolean admin) {
        Admin = admin;
    }


    public String getSender_id() {
        return Sender_id;
    }

    public void setSender_id(String sender_id) {
        Sender_id = sender_id;
    }

    public String getGo_Activity() {
        return Go_Activity;
    }

    public void setGo_Activity(String go_Activity) {
        Go_Activity = go_Activity;
    }

    public String getCollection() {
        return Collection;
    }

    public void setCollection(String collection) {
        Collection = collection;
    }

    public Product_class getProduct_class() {
        return product_class;
    }

    public void setProduct_class(Product_class product_class) {
        this.product_class = product_class;
    }
}
