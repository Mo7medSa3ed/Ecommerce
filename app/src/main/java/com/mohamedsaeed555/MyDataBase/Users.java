package com.mohamedsaeed555.MyDataBase;

import java.util.ArrayList;

public class Users {
    private String token;
    private Users user;
    private ArrayList<String> fav;
    private String name;
    private String tel;
    private String adress;
    private String image;
    private String email;
    private String password;
    private String city;
    private String fbid;
    private String goid;
    private Boolean admin;
    private Boolean superAdmin;
    private String _id;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<String> getFav() {
        return fav;
    }

    public void setFav(ArrayList<String> fav) {
        this.fav = fav;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getGoid() {
        return goid;
    }

    public void setGoid(String goid) {
        this.goid = goid;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Users(String name, String tel, String adress, String image, String email, String password, String city, String fbid, String goid, Boolean admin, Boolean superAdmin, String _id,String token) {
        this.name = name;
        this.tel = tel;
        this.adress = adress;
        this.image = image;
        this.email = email;
        this.password = password;
        this.city = city;
        this.fbid = fbid;
        this.goid = goid;
        this.admin = admin;
        this.superAdmin = superAdmin;
        this._id = _id;
        this.token=token;
    }

    public Users(String name, String tel, String adress, String image, String email, String password, String city, String fbid, String goid, Boolean admin, Boolean superAdmin, String _id) {
        this.name = name;
        this.tel = tel;
        this.adress = adress;
        this.image = image;
        this.email = email;
        this.password = password;
        this.city = city;
        this.fbid = fbid;
        this.goid = goid;
        this.admin = admin;
        this.superAdmin = superAdmin;
        this._id = _id;
    }

    public Users(String name, String tel, String adress, String image, String email , String _id) {
        this.name = name;
        this.tel = tel;
        this.adress = adress;
        this.image = image;
        this.email = email;
        this._id=_id;
    }

    public Users(String name, String tel, String adress, String city) {
        this.name = name;
        this.tel = tel;
        this.adress = adress;
        this.city = city;
    }

    public Users(String name, String tel, String adress, String image, String email, String password, String city, String fbid, String goid, Boolean admin, Boolean superAdmin) {
        this.name = name;
        this.tel = tel;
        this.adress = adress;
        this.image = image;
        this.email = email;
        this.password = password;
        this.city = city;
        this.fbid = fbid;
        this.goid = goid;
        this.admin = admin;
        this.superAdmin = superAdmin;
    }

    public Users(ArrayList<String> fav, String name, String tel, String adress, String image, String email, String password, String city, String fbid, String goid, Boolean admin, Boolean superAdmin, String _id) {
        this.fav = fav;
        this.name = name;
        this.tel = tel;
        this.adress = adress;
        this.image = image;
        this.email = email;
        this.password = password;
        this.city = city;
        this.fbid = fbid;
        this.goid = goid;
        this.admin = admin;
        this.superAdmin = superAdmin;
        this._id = _id;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(Boolean superAdmin) {
        this.superAdmin = superAdmin;
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


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFb_id() {
        return fbid;
    }

    public void setFb_id(String fb_id) {
        this.fbid = fb_id;
    }

    public String getGo_id() {
        return goid;
    }

    public void setGo_id(String go_id) {
        this.goid = go_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
