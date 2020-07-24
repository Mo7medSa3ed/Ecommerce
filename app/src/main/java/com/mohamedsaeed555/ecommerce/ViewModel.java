package com.mohamedsaeed555.ecommerce;

import androidx.lifecycle.MutableLiveData;

import com.mohamedsaeed555.MyDataBase.Product_class;

import java.util.List;

public class ViewModel extends androidx.lifecycle.ViewModel {
    MutableLiveData<List<Product_class>> liveData_getallproducts = new MutableLiveData<>();


}
