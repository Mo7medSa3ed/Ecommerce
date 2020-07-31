package com.mohamedsaeed555.ecommerce;

import com.mohamedsaeed555.MyDataBase.Orders;
import com.mohamedsaeed555.MyDataBase.Poset_Orders;
import com.mohamedsaeed555.MyDataBase.Product_class;
import com.mohamedsaeed555.MyDataBase.Retrofit_class_data;
import com.mohamedsaeed555.MyDataBase.Users;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    @GET("product/")
    Call<List<Product_class>> GETALLPRODUCTS(@Header("x-auth-token") String token);

    @GET("product/{collection_name}/products/all")
    Call<List<Product_class>> GETALLPRODUCTSINCOLLECTION(@Header("x-auth-token") String token, @Path("collection_name") String collection_name);


    @GET("product/{collection_name}/{page_number}")
    Call<Retrofit_class_data> GETPRODUCTPAGINATION(@Header("x-auth-token") String token, @Path("collection_name") String collection_name, @Path("page_number") String page_number);

    @GET("product/one/{barcode}")
    Call<One_product_class> GETONEPRODUCTDETAILS(@Header("x-auth-token") String token, @Path("barcode") String barcode);


    @Multipart
    @POST("product/{collection_name}/")
    Call<Product_class> ADD_PRODUCT(@Header("x-auth-token") String token,
                                    @Path("collection_name") String collection_name,
                                    @Part("date") String date,
                                    @Part("amount") Integer amount,
                                    @Part("barcode") RequestBody old_barcode,
                                    @Part("name") RequestBody name,
                                    @Part("price") double price,
                                    @Part("brand") RequestBody brand,
                                    @Part MultipartBody.Part image);


    @DELETE("product/{collection_name}/{barcode}")
    Call<Void> DELETEPRODUCT(@Header("x-auth-token") String token, @Path("collection_name") String collection_name, @Path("barcode") String barcode);


    @Multipart
    @PUT("product/{collection_name}")
    Call<Product_class> UPDATEPRODUCT(@Header("x-auth-token") String token,
                                      @Path("collection_name") String collection_name,
                                      @Part("date") String date,
                                      @Part("amount") Integer amount,
                                      @Part("barcode") RequestBody old_barcode,
                                      @Part("name") RequestBody name,
                                      @Part("price") double price,
                                      @Part("brand") RequestBody brand,
                                      @Part MultipartBody.Part image);


    @FormUrlEncoded
    @POST("product/{collection_name}/filter")
    Call<List<Product_class>> GETSEARCHRODUCTNAME(@Header("x-auth-token") String token, @Path("collection_name") String collection_name, @Field("name") String name);


    @FormUrlEncoded
    @POST("product/barcode/{barcode}")
    Call<List<Product_class>> GETSEARCHRODUCTBARCODE(@Header("x-auth-token") String token, @Path("barcode") String collection_name);


    @FormUrlEncoded
    @PUT("product/{collection_name}/amount")
    Call<Void> UPDATEAMOUNT(@Header("x-auth-token") String token, @Path("collection_name") String collection_name, @Field("barcode") String barcode, @Field("amount") int amount);

/////////////////////////////////////////////////////////////////////////////////////////////////////

    @POST("users/")
    Call<Users> ADDUSER(@Body Users users);


    @POST("users/login")
    Call<Users> LOGINUSER(@Body Users users);


    @GET("users/all")
    Call<List<Users>> GETALLUSERS(@Header("x-auth-token") String token);


    @GET("users/one")
    Call<Users> GETONEUSER(@Header("x-auth-token") String token);

    @Multipart
    @PATCH("users/{id}")
    Call<Users> UPDATEUSER(@Header("x-auth-token") String token,
                           @Path("id") String id,
                           @Part("name") RequestBody name,
                           @Part("tel") RequestBody tel,
                           @Part("adress") RequestBody adress,
                           @Part("city") RequestBody city,
                           @Part("email") RequestBody email,
                           @Part MultipartBody.Part image,
                           @Part("admin") Boolean Admin,
                           @Part("superAdmin") Boolean SuperAdmin,
                           @Part("fbid") RequestBody fbid,
                           @Part("goid") RequestBody goid);


    @PATCH("users/{id}")
    Call<Users> UPDATEUSER2(@Header("x-auth-token") String token, @Path("id") String id, @Body Users users);

    @FormUrlEncoded
    @PATCH("users/{id}")
    Call<Users> UPDATEUSERADMIN(@Header("x-auth-token") String token, @Path("id") String id, @Field("admin") Boolean admin);


    @FormUrlEncoded
    @PATCH("users/{id}")
    Call<Users> UPDATEUSER_Fav(@Header("x-auth-token") String token, @Path("id") String id, @Field("fav") ArrayList<String> fav);


    @FormUrlEncoded
    @PATCH("users/{id}")
    Call<Users> CHANGEPASSWORD(@Header("x-auth-token") String token, @Path("id") String id, @Field("password") String password);


    @DELETE("users/{id}")
    Call<Void> DELETEUSER(@Header("x-auth-token") String token, @Path("id") String id);


    @DELETE("users/delete/all")
    Call<Void> DELETEALLUSER(@Header("x-auth-token") String token);

/////////////////////////////////////////////////////////////////////////////////////////////////////

    @POST("orders/")
    Call<Poset_Orders> POSTORDER(@Header("x-auth-token") String token, @Body Poset_Orders orders);


    @DELETE("orders/{id}")
    Call<Void> DELETEONEORDER(@Header("x-auth-token") String token, @Path("id") String id);


    @DELETE("orders/orders/all")
    Call<Void> DELETEALLORDER(@Header("x-auth-token") String token);


    @PATCH("orders/{id}")
    Call<Orders> UPDATEORDER(@Header("x-auth-token") String token, @Path("id") String id, @Body Orders orders);

    @FormUrlEncoded
    @PATCH("orders/{id}")
    Call<Orders> UPDATEORDERPAID(@Header("x-auth-token") String token, @Path("id") String id, @Field("paid") boolean paid , @Field("paidAt") Date date);

    @FormUrlEncoded
    @PATCH("orders/{id}")
    Call<Orders> UPDATEORDERDELEVIRD(@Header("x-auth-token") String token, @Path("id") String id, @Field("delivery") boolean paid , @Field("deliveryAt") Date date);


    @GET("orders/")
    Call<List<Orders>> GETALLORDERS(@Header("x-auth-token") String token);

    @GET("orders/{id}")
    Call<Orders> GETONEORDERS(@Header("x-auth-token") String token, @Path("id") String id);
}
