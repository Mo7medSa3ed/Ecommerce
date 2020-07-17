package com.mohamedsaeed555.ecommerce;

import androidx.lifecycle.MutableLiveData;

import com.mohamedsaeed555.MyDataBase.Product_class;
import com.mohamedsaeed555.MyDataBase.Retrofit_class_data;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewModel extends androidx.lifecycle.ViewModel {
    MutableLiveData <List<Product_class>> liveData_getallproducts =new MutableLiveData<>();


    public void GET_Products(String collection_name){
        RetrofitClient.getInstance().GETALLPRODUCTS(collection_name).enqueue(new Callback<List<Product_class>>() {
            @Override
            public void onResponse(Call<List<Product_class>> call, Response<List<Product_class>> response) {
                liveData_getallproducts.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Product_class>> call, Throwable t) {

            }
        });
    }



}
