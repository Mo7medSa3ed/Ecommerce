package com.mohamedsaeed555.ecommerce;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mohamedsaeed555.MyDataBase.Orders;
import com.mohamedsaeed555.MyDataBase.Poset_Orders;
import com.mohamedsaeed555.MyDataBase.Product_class;
import com.mohamedsaeed555.MyDataBase.Retrofit_class_data;
import com.mohamedsaeed555.MyDataBase.Users;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://newaccsys.herokuapp.com/";
    private static RetrofitClient INSTANCE;
    private Retrofit_Interface retrofit_interface;


    public RetrofitClient() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        retrofit_interface = retrofit.create(Retrofit_Interface.class);

    }

    public static RetrofitClient getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new RetrofitClient();
        }
        return INSTANCE;
    }

    public Call<Product_class> ADDPRODUCT(String token, String coll_name, String date1, int amount1, RequestBody barcode, RequestBody name1, double price1, RequestBody brand1, MultipartBody.Part image) {

        return retrofit_interface.ADD_PRODUCT(token, coll_name, date1, amount1, barcode, name1, price1, brand1, image);
    }

    public Call<Product_class> UPDATEPRODUCT(String token, String coll_name, String date1, int amount1, RequestBody barcode, RequestBody name1, double price1, RequestBody brand1, MultipartBody.Part image) {

        return retrofit_interface.UPDATEPRODUCT(token, coll_name, date1, amount1, barcode, name1, price1, brand1, image);
    }

    public Call<List<Product_class>> GETALLPRODUCTS(String token) {

        return retrofit_interface.GETALLPRODUCTS(token);
    }

    public Call<Retrofit_class_data> GETALLPRODUCTSPAGINATION(String token, String collection_name, String page_number) {

        return retrofit_interface.GETPRODUCTPAGINATION(token, collection_name, page_number);
    }

    public Call<List<Product_class>> GETALLPRODUCTSCOLLECTION(String token, String collection_name) {
        return retrofit_interface.GETALLPRODUCTSINCOLLECTION(token, collection_name);
    }
    public Call<One_product_class> GETONEPRODUCTDETAILS(String token, String barcode) {
        return retrofit_interface.GETONEPRODUCTDETAILS(token, barcode);
    }

    public Call<Void> DELETEPRODUCT(String token, String collection_name, String barcode) {
        return retrofit_interface.DELETEPRODUCT(token, collection_name, barcode);
    }

    public Call<List<Product_class>> GETSEARCHRODUCTNAME(String token, String collection_name, String name) {
        return retrofit_interface.GETSEARCHRODUCTNAME(token, collection_name, name);
    }

    public Call<List<Product_class>> GETSEARCHRODUCTBARCODE(String token, String barcode) {
        return retrofit_interface.GETSEARCHRODUCTBARCODE(token, barcode);
    }

    public Call<Void> UPDATEAMOUNTFORPRODUCT(String token, String collection_name, String barcode, int amount) {
        return retrofit_interface.UPDATEAMOUNT(token, collection_name, barcode, amount);
    }
////////////////////////////////////////////////////////////////////////////////////////

    public Call<Users> Add_User(Users users) {
        return retrofit_interface.ADDUSER(users);
    }

    public Call<Users> Login_User(Users users) {
        return retrofit_interface.LOGINUSER(users);
    }

    public Call<Users> GET_one_User(String token) {
        return retrofit_interface.GETONEUSER(token);
    }

    public Call<List<Users>> GET_all_User(String token) {
        return retrofit_interface.GETALLUSERS(token);
    }

    public Call<Users> UpdateUser(String token, String _id, RequestBody name, RequestBody tel, RequestBody adress, RequestBody city, RequestBody email, MultipartBody.Part image, Boolean admin, Boolean superAdmin, RequestBody fbid, RequestBody goid) {
        return retrofit_interface.UPDATEUSER(token, _id, name, tel, adress, city, email, image, admin, superAdmin, fbid, goid);
    }

    public Call<Users> UpdateUser2(String token, String _id, Users users) {
        return retrofit_interface.UPDATEUSER2(token, _id, users);
    }

    public Call<Users> UpdateUserAdmin(String token, String _id, Boolean admin) {
        return retrofit_interface.UPDATEUSERADMIN(token, _id, admin);
    }


    public Call<Users> UpdateUser_Fav(String token, String _id, ArrayList<String> fav) {
        return retrofit_interface.UPDATEUSER_Fav(token, _id, fav);
    }

    public Call<Users> ChangePassword(String token, String _id, String password) {
        return retrofit_interface.CHANGEPASSWORD(token, _id, password);
    }


    public Call<Void> DeleteUser(String token, String _id) {
        return retrofit_interface.DELETEUSER(token, _id);
    }

    public Call<Void> DeleteAllUser(String token) {
        return retrofit_interface.DELETEALLUSER(token);
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////


    public Call<Poset_Orders> PostOrder(String token, Poset_Orders orders) {
        return retrofit_interface.POSTORDER(token, orders);
    }


    public Call<Orders> UpdateOrder(String token, String id, Orders orders) {
        return retrofit_interface.UPDATEORDER(token, id, orders);
    }


    public Call<Orders> UpdateOrderPaid(String token, String id, boolean paid, Date date) {
        return retrofit_interface.UPDATEORDERPAID(token, id, paid, date);
    }

    public Call<Orders> UpdateOrderDeleviry(String token, String id, boolean paid, Date date) {
        return retrofit_interface.UPDATEORDERDELEVIRD(token, id, paid, date);
    }


    public Call<List<Orders>> GetAllOrder(String token) {
        return retrofit_interface.GETALLORDERS(token);
    }

    public Call<Orders> GetOneOrder(String token, String id) {
        return retrofit_interface.GETONEORDERS(token, id);
    }


    public Call<Void> DeleteAllOrder(String token) {
        return retrofit_interface.DELETEALLORDER(token);
    }


    public Call<Void> DeleteOneOrder(String token, String id) {
        return retrofit_interface.DELETEONEORDER(token, id);
    }

}
