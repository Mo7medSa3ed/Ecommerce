package com.mohamedsaeed555.ecommerce;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mohamedsaeed555.MyDataBase.Orders;
import com.mohamedsaeed555.MyDataBase.Poset_Orders;
import com.mohamedsaeed555.MyDataBase.Product_class;
import com.mohamedsaeed555.MyDataBase.Retrofit_class_data;
import com.mohamedsaeed555.MyDataBase.Users;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Retrofit_Interface {

    @GET("{collection_name}/products/all")
    Call<List<Product_class>> GETALLPRODUCTS(@Path("collection_name") String collection_name);

    @GET("{collection_name}/{page_number}")
    Call<Retrofit_class_data> GETPRODUCTPAGINATION(@Path("collection_name") String collection_name, @Path("page_number") String page_number);

    @GET("{collection_name}/one/{barcode}")
    Call<One_product_class> GETONEPRODUCTDETAILS(@Path("collection_name")String collection_name,@Path("barcode")String barcode);


    @Multipart
    @POST("{collection_name}")
    Call<Product_class> ADD_PRODUCT(@Path("collection_name")String collection_name,
                                   @Part("date") String date,
                                   @Part("amount") Integer amount,
                                   @Part("barcode") RequestBody old_barcode,
                                   @Part("name") RequestBody name,
                                   @Part("price") double price,
                                   @Part("brand") RequestBody brand,
                                   @Part MultipartBody.Part image);


    @DELETE("{collection_name}/{barcode}")
    Call<Void> DELETEPRODUCT(@Path("collection_name")String collection_name,@Path("barcode") String barcode);


    @Multipart
    @PUT("{collection_name}")
    Call<Product_class> UPDATEPRODUCT(@Header("x-auth-token") String token,
                                      @Path("collection_name")String collection_name,
                                      @Part("date") String date,
                                      @Part("amount") Integer amount,
                                      @Part("barcode") RequestBody old_barcode,
                                      @Part("name") RequestBody name,
                                      @Part("price") double price,
                                      @Part("brand") RequestBody brand,
                                      @Part MultipartBody.Part image);


    @FormUrlEncoded
    @POST("{collection_name}/filter")
    Call<List<Product_class>> GETSEARCHRODUCTNAME(@Path("collection_name")String collection_name,@Field("name")String name);


    @FormUrlEncoded
    @POST("{collection_name}/filter")
    Call<List<Product_class>> GETSEARCHRODUCTBARCODE(@Path("collection_name")String collection_name,@Field("barcode")String barcode);


    @FormUrlEncoded
    @PUT("{collection_name}/amount")
    Call<Void> UPDATEAMOUNT(@Path("collection_name") String collection_name , @Field("barcode") String barcode , @Field("amount") int amount);

/////////////////////////////////////////////////////////////////////////////////////////////////////

    @POST("users/")
    Call<Users> ADDUSER (@Body Users users);


    @POST("users/login")
    Call<Users> LOGINUSER (@Body Users users);


    @GET("users/all")
    Call<List<Users>> GETALLUSERS ();


    @GET("users/one")
    Call<Users> GETONEUSER ();

    @Multipart
    @PATCH("users/{id}")
    Call<Users> UPDATEUSER(@Path("id") String id,
                           @Part("name") RequestBody name,
                           @Part("tel") RequestBody tel,
                           @Part("adress") RequestBody adress,
                           @Part("city") RequestBody city,
                           @Part("email") RequestBody email,
                           @Part MultipartBody.Part image,
                           @Part("admin") Boolean Admin ,
                           @Part("superAdmin") Boolean SuperAdmin ,
                           @Part("fbid") RequestBody fbid,
                           @Part("goid") RequestBody goid);


    @PATCH("users/{id}")
    Call<Users> UPDATEUSER2(@Path("id") String id,@Body Users users);


    @FormUrlEncoded
    @PATCH("users/{id}")
    Call<Users> UPDATEUSER_Fav(@Path("id") String id, @Field("fav") ArrayList<String> fav);


    @FormUrlEncoded
    @PATCH("users/{id}")
    Call<Users> CHANGEPASSWORD(@Path("id") String id, @Field("password") String password);





    @DELETE("users/{id}")
    Call<Void> DELETEUSER(@Path("id") String id);


    @DELETE("users/delete/all")
    Call<Void> DELETEALLUSER();

/////////////////////////////////////////////////////////////////////////////////////////////////////

    @POST("orders/")
    Call<Poset_Orders> POSTORDER (@Body Poset_Orders  orders);



    @DELETE("orders/{id}")
    Call<Void> DELETEONEORDER (@Path("id") String id);


    @DELETE("orders/orders/all")
    Call<Void> DELETEALLORDER ();


    @PATCH("orders/{id}")
    Call<Orders> UPDATEORDER (@Path("id") String id, @Body Orders orders);


    @PATCH("orders/{id}")
    Call<Orders> UPDATEORDERPAID (@Path("id") String id, @Body JSONObject orders);


    @PATCH("orders/{id}")
    Call<Orders> UPDATEORDERDELEVIRD (@Path("id") String id,@Body JSONObject jsonObject);



    @GET("orders/")
    Call<List<Orders>> GETALLORDERS ();

    @GET("orders/{id}")
        Call<Orders> GETONEORDERS (@Path("id")String id);
}
