package com.mohamedsaeed555.MyDataBase;

import java.util.ArrayList;

public class Poset_Orders {
    private ArrayList<ObjectProduct> products;
    private ArrayList<Double> total;
    private Boolean delivery;
    private String deliveryAt;
    private Boolean paid;
    private String paidAt;
    private String _id;
    private User by;

    public Poset_Orders(User by, ArrayList<ObjectProduct> products, ArrayList<Double> total) {
        this.by = by;
        this.products = products;
        this.total = total;
    }

    public Poset_Orders(Boolean paid, String paidAt) {
        this.paid = paid;
        this.paidAt = paidAt;
    }


    public ArrayList<Double> getTotal() {
        return total;
    }

    public void setTotal(ArrayList<Double> total) {
        this.total = total;
    }

    public User getBy() {
        return by;
    }

    public void setBy(User by) {
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


    public static class User {
        private String name;
        private String email;
        private String tel;
        private String adress;
        private String city;
        private String _id;

        public User(String name, String email, String tel, String adress, String city,String _id) {
            this.name = name;
            this.email = email;
            this.tel = tel;
            this.adress = adress;
            this.city = city;
            this._id=_id;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getAdress() {
            return adress;
        }

        public void setAdress(String adress) {
            this.adress = adress;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }

}