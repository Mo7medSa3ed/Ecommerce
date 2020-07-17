package com.mohamedsaeed555.ecommerce;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.JsonObject;
import com.mohamedsaeed555.MyDataBase.Orders;
import com.mohamedsaeed555.MyDataBase.Poset_Orders;
import com.mohamedsaeed555.MyDataBase.Product_class;
import com.mohamedsaeed555.MyDataBase.Retrofit_class_data;
import com.mohamedsaeed555.MyDataBase.Users;

import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;

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

    public Call<Product_class>ADDPRODUCT(String coll_name,  String date1, int amount1 , RequestBody barcode , RequestBody name1 ,  double price1, RequestBody brand1 ,MultipartBody.Part image) {

        return retrofit_interface.ADD_PRODUCT(coll_name ,date1,amount1,barcode,name1,price1,brand1,image);
    }

    public Call<Product_class>UPDATEPRODUCT(String token,String coll_name,  String date1, int amount1 , RequestBody barcode , RequestBody name1 ,  double price1, RequestBody brand1 ,MultipartBody.Part image ) {

        return retrofit_interface.UPDATEPRODUCT(token,coll_name ,date1,amount1,barcode,name1,price1,brand1,image);
    }

    public Call<List<Product_class>>GETALLPRODUCTS(String collection_name) {

        return retrofit_interface.GETALLPRODUCTS(collection_name);
    }

    public Call<Retrofit_class_data>GETALLPRODUCTSPAGINATION(String collection_name , String page_number) {

        return retrofit_interface.GETPRODUCTPAGINATION(collection_name,page_number);
    }

    public Call<One_product_class> GETONEPRODUCTDETAILS(String collection_name,String barcode){
        return retrofit_interface.GETONEPRODUCTDETAILS(collection_name,barcode);
    }

    public Call<Void> DELETEPRODUCT( String collection_name,String barcode){
        return retrofit_interface.DELETEPRODUCT(collection_name,barcode);
    }

    public Call<List<Product_class>> GETSEARCHRODUCTNAME(String collection_name,String name){
        return retrofit_interface.GETSEARCHRODUCTNAME(collection_name,name);
    }

    public Call<List<Product_class>> GETSEARCHRODUCTBARCODE(String collection_name,String barcode){
        return retrofit_interface.GETSEARCHRODUCTBARCODE(collection_name,barcode);
    }

    public Call<Void> UPDATEAMOUNTFORPRODUCT(String collection_name,String barcode , int amount){
        return retrofit_interface.UPDATEAMOUNT(collection_name,barcode,amount);
    }
////////////////////////////////////////////////////////////////////////////////////////

    public Call<Users> Add_User (Users users){
        return retrofit_interface.ADDUSER(users);
    }

    public Call<Users> Login_User (Users users){
        return retrofit_interface.LOGINUSER(users);
    }

    public Call<Users> GET_one_User (){
        return retrofit_interface.GETONEUSER();
    }

    public Call<List<Users>> GET_all_User (){
        return retrofit_interface.GETALLUSERS();
    }

    public Call<Users> UpdateUser (String _id ,RequestBody name ,RequestBody tel ,RequestBody adress ,RequestBody city ,RequestBody email ,MultipartBody.Part image ,Boolean admin ,Boolean superAdmin,RequestBody fbid ,RequestBody goid){
        return retrofit_interface.UPDATEUSER(_id,name,tel,adress,city,email,image,admin,superAdmin,fbid,goid);
    }

    public Call<Users> UpdateUser2 (String _id ,Users users){
        return retrofit_interface.UPDATEUSER2(_id,users);
    }


    public Call<Users> UpdateUser_Fav (String _id ,ArrayList<String> fav){
        return retrofit_interface.UPDATEUSER_Fav(_id,fav);
    }

    public Call<Users> ChangePassword (String _id ,String password){
        return retrofit_interface.CHANGEPASSWORD(_id,password);
    }


    public Call<Void> DeleteUser (String _id){
        return retrofit_interface.DELETEUSER(_id);
    }

    public Call<Void> DeleteAllUser (){
        return retrofit_interface.DELETEALLUSER();
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////


    public Call<Poset_Orders> PostOrder(Poset_Orders orders){
        return retrofit_interface.POSTORDER(orders);
    }


    public Call<Orders> UpdateOrder(String id, Orders orders){
        return retrofit_interface.UPDATEORDER(id,orders);
    }


    public Call<Orders> UpdateOrderPaid(String id, JSONObject orders){
        return retrofit_interface.UPDATEORDERPAID(id,orders);
    }

    public Call<Orders> UpdateOrderDeleviry(String id, JSONObject jsonObject){
        return retrofit_interface.UPDATEORDERDELEVIRD(id,jsonObject);
    }


    public Call<List<Orders>> GetAllOrder(){
        return retrofit_interface.GETALLORDERS();
    }

    public Call<Orders> GetOneOrder(String id){
        return retrofit_interface.GETONEORDERS(id);
    }


    public Call<Void> DeleteAllOrder(){
        return retrofit_interface.DELETEALLORDER();
    }


    public Call<Void> DeleteOneOrder(String id){
        return retrofit_interface.DELETEONEORDER(id);
    }

}
