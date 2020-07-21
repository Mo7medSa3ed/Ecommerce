package com.mohamedsaeed555.ecommerce;


import android.icu.util.LocaleData;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.DetailsProductOrder;
import com.mohamedsaeed555.MyDataBase.ObjectProduct;
import com.mohamedsaeed555.MyDataBase.Orders;
import com.mohamedsaeed555.MyDataBase.Poset_Orders;
import com.mohamedsaeed555.MyDataBase.Product_class;
import com.mohamedsaeed555.MyDataBase.Users;
import com.mohamedsaeed555.Notification.Notification_Class;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Orders_Details extends Fragment {

    ListView list;
    Button paid , contact;
    ImageView cimage;
    TextView price , cname, ccity, cphone , caddress ;
    Database db;
    Users  users;
    Boolean pid =false;
    Boolean dele =false;
    String id = "";
    DetailsOrderAdapter adapter;
    String total = "";
    ArrayList<ObjectProduct> products =new ArrayList<>();
    ArrayList<ObjectProduct> new_pr =new ArrayList<>();
    ArrayList<DetailsProductOrder> data =new ArrayList<>();
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://newaccsys.herokuapp.com");
        } catch (URISyntaxException e) {}
    }
    Gson gson =new Gson();
    Date date  =new Date();
    Orders o;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       try {
           db = new Database(getActivity());
           users = db.getAllusers().get(0);
           o = gson.fromJson(getArguments().getString("order"), Orders.class);
           id = o.get_id();
           pid = o.getPaid();
           dele = o.getDelivery();
           total = String.valueOf(o.getTotal().get(0));
           if (o == null) {
               o = gson.fromJson(getArguments().getString("o"), Orders.class);
               id = o.get_id();
               pid = o.getPaid();
               dele = o.getDelivery();
               total = String.valueOf(o.getTotal().get(0));
           }
       }catch (Exception e){e.printStackTrace();}

        return inflater.inflate(R.layout.fragment_orders__details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Order Details");

        list = view.findViewById(R.id.listdetails);
        paid = view.findViewById(R.id.button8);
        contact = view.findViewById(R.id.button9);
        price = view.findViewById(R.id.textView46);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.order_header,list,false);
        list.addHeaderView(header);
        cname = header.findViewById(R.id.textView14);
        ccity = header.findViewById(R.id.textView41);
        cphone = header.findViewById(R.id.textView42);
        caddress = header.findViewById(R.id.textView44);
        cimage = header.findViewById(R.id.imageView11);
        price.setText(total + " EGP");
        cname.setText(users.getName());
        ccity.setText(users.getCity());
        cphone.setText(users.getTel());
        caddress.setText(users.getAdress());

        if (db.getAllusers().get(0).getToken()!= null){
            paid.setVisibility(View.GONE);
            contact.setVisibility(View.GONE);
        }


        if (pid){
            cimage.setImageResource(R.drawable.ic_check_black_24dp);
            paid.setClickable(false);
            paid.setEnabled(false);
            paid.setBackgroundResource(R.drawable.btn_shape_enable);
        }else {
            cimage.setImageResource(R.drawable.ic_clear_black_24dp);
        }


        if (dele){
            contact.setClickable(false);
            contact.setEnabled(false);
            contact.setBackgroundResource(R.drawable.btn_shape_enable);
        }



        adapter = new DetailsOrderAdapter(data,getActivity());
        list.setAdapter(adapter);
        GETDATA();

        paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject =new JSONObject();
                try {
                    jsonObject.accumulate("paid",true);
                    jsonObject.accumulate("paidAt",getDate());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                updatepaid(id,  jsonObject);

            }
        });


        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject =new JSONObject();
                try {
                    jsonObject.put("delivery",true);
                    jsonObject.put("deliveryAt",getDate());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                updatecontact(id,jsonObject);

            }
        });


    }

    private void GETDATA (){
        RetrofitClient.getInstance().GetOneOrder(users.getToken(),id).enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders>response) {
                if (response.isSuccessful()){

                    products.clear();
                    data.clear();
                    products = response.body().getProducts();
                    new_pr.add(response.body().getProducts().get(0));

                    for (int x=1; x<products.size(); x++){
                        if (products.get(x).getBarcode().equals(new_pr.get(x-1).getBarcode())){
                            int old_amount = products.get(x).getAmount();
                            int amount = new_pr.get(x-1).getAmount();
                            new_pr.get(x-1).setAmount(old_amount+amount);
                        }else {
                            new_pr.add(products.get(x));
                        }
                    }


                    for (int x=0; x<new_pr.size(); x++){
                        if (db.Search_product2("AllData",new_pr.get(x).getBarcode()).size()>0){
                            Product_class p = db.Search_product2("AllData",new_pr.get(x).getBarcode()).get(0);
                            data.add(new DetailsProductOrder(p,new_pr.get(x).getAmount()));
                        }
                    }


                    adapter.setdata(data,getActivity());


                }else{

                    Toast.makeText(getActivity(),String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(),response.errorBody().toString(),Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t) {
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }

    private void updatepaid(String id , JSONObject orders){
        RetrofitClient.getInstance().UpdateOrderPaid(users.getToken(),id,orders).enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response) {
                if (response.isSuccessful()){
                    Notification_Class notification_class =new Notification_Class(users.getAdmin(),"Admin Contact with your order","orderdetails",o);

                    mSocket.emit("dbchanged",gson.toJson(notification_class));

                    Toast.makeText(getActivity(),String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                    paid.setClickable(false);
                    paid.setEnabled(false);
                }else {
                    Toast.makeText(getActivity(),String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                    paid.setClickable(true);
                    paid.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t) {
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                paid.setClickable(true);
                paid.setEnabled(true);
            }
        });
    }

    private void updatecontact(String id , JSONObject jsonObject){
        RetrofitClient.getInstance().UpdateOrderDeleviry(users.getToken(),id,jsonObject).enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response) {
                if (response.isSuccessful()){
                    Notification_Class notification_class =new Notification_Class(users.getAdmin(),"Admin Contact with your order","orderdetails",o);
                    mSocket.emit("dbchanged",gson.toJson(notification_class));

                    Toast.makeText(getActivity(),String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                    contact.setClickable(false);
                    contact.setEnabled(false);
                }else {
                    Toast.makeText(getActivity(),String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                    contact.setClickable(true);
                    contact.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t) {
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                contact.setClickable(true);
                contact.setEnabled(true);
            }
        });
    }

    private  String getDate(){
        DateFormat dtf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dtf.format(date);
    }
}
