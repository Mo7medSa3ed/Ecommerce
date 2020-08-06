package com.mohamedsaeed555.ecommerce;


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
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.DetailsProductOrder;
import com.mohamedsaeed555.MyDataBase.ObjectProduct;
import com.mohamedsaeed555.MyDataBase.Orders;
import com.mohamedsaeed555.MyDataBase.Poset_Orders;
import com.mohamedsaeed555.MyDataBase.Poset_Orders.User;
import com.mohamedsaeed555.MyDataBase.Product_class;
import com.mohamedsaeed555.MyDataBase.Users;
import com.mohamedsaeed555.Notification.Notification_Class;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Orders_Details extends Fragment {

    ListView list;
    Button paid, contact;
    ImageView cimage;
    TextView price, cname, ccity, cphone, caddress;
    String oprice, oname, ocity, ophone, oaddress;
    Database db;
    Users users ,order_user;
     DetailsProductOrder n;
    Boolean pid = false;
    Boolean dele = false;
    String id = "";
    DetailsOrderAdapter adapter;
    String total = "";
    ArrayList<ObjectProduct> products = new ArrayList<>();
    ArrayList<ObjectProduct> new_pr = new ArrayList<>();
    ArrayList<ObjectProduct> test = new ArrayList<>();
    ArrayList<DetailsProductOrder> data = new ArrayList<>();
    Gson gson = new Gson();
    Date date = new Date();
    Orders o;
    int x=0;
    Poset_Orders o2;
    User user ;
    String oid;
    Boolean admin=false;
    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("https://newaccsys.herokuapp.com");
        } catch (URISyntaxException e) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new Database(getActivity());
        users = db.getAllusers().get(0);
       try {
           o = gson.fromJson(getArguments().getString("order"), Orders.class);
           if (o != null) {
               id = o.get_id();
               pid = o.getPaid();
               dele = o.getDelivery();
               total = String.valueOf(o.getTotal().get(0));
               order_user=o.getBy();
               oname=order_user.getName();
               ocity=order_user.getCity();
               ophone=order_user.getTel();
               oaddress=order_user.getAdress();
               oid=order_user.get_id();

           } else {
               o2 = gson.fromJson(getArguments().getString("os"), Poset_Orders.class);
               id = o2.get_id();
               pid = o2.getPaid();
               dele = o2.getDelivery();
               total = String.valueOf(o2.getTotal().get(0));
               user = o2.getBy();
               oname=user.getName();
               ocity=user.getCity();
               ophone=user.getTel();
               oaddress=user.getAdress();
               oid=user.get_id();
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
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.order_header, list, false);
        list.addHeaderView(header);
        cname = header.findViewById(R.id.textView14);
        ccity = header.findViewById(R.id.textView41);
        cphone = header.findViewById(R.id.textView42);
        caddress = header.findViewById(R.id.textView44);
        cimage = header.findViewById(R.id.imageView11);
        price.setText(total + " EGP");
        cname.setText(oname);
        ccity.setText(ocity);
        cphone.setText(ophone);
        caddress.setText(oaddress);

        if (db.getAllusers().get(0).getToken() == null) {
            paid.setVisibility(View.GONE);
            contact.setVisibility(View.GONE);
        }


        if (pid) {
            cimage.setImageResource(R.drawable.ic_check_black_24dp);
            paid.setClickable(false);
            paid.setEnabled(false);
            paid.setBackgroundResource(R.drawable.btn_shape_enable);
        } else {
            cimage.setImageResource(R.drawable.ic_clear_black_24dp);
        }


        if (dele) {
            contact.setClickable(false);
            contact.setEnabled(false);
            contact.setBackgroundResource(R.drawable.btn_shape_enable);
        }


        adapter = new DetailsOrderAdapter(data, getActivity());
        list.setAdapter(adapter);

       // Toast.makeText(getActivity(),id,Toast.LENGTH_LONG).show();
        GETDATA();

        paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updatepaid(id);

            }
        });


        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updatecontact(id);

            }
        });


    }

    private void GETDATA() {
        RetrofitClient.getInstance().GetOneOrder(null, id).enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response) {
                if (response.isSuccessful()) {

                    products.clear();
                    data.clear();
                    new_pr.clear();
                    products = response.body().getProducts();
                    try {
                        if (products.size() > 0) {
                            new_pr.add(response.body().getProducts().get(0));
                        }
                        if (products.size() > 1) {
                            for (int x = 1; x < products.size(); x++) {
                                for (int i=0 ; i<new_pr.size(); i++) {
                                    if (products.get(x).getBarcode().equals(new_pr.get(i).getBarcode())) {
                                        int old_amount = products.get(x).getAmount();
                                        int amount = new_pr.get(i).getAmount();
                                        new_pr.get(i).setAmount(old_amount+amount);
                                        break;
                                    } else {
                                        Boolean test =null;
                                        for (int z =0 ;z<new_pr.size();z++ ){
                                          if (products.get(x).getBarcode().equals(new_pr.get(z).getBarcode())){
                                                test =true;
                                          }else {test =false;}

                                        }
                                        if (test == false){
                                            new_pr.add(products.get(x));break;
                                        }
                                    }
                                }
                            }
                        }
                    }catch (Exception e){e.printStackTrace();}


                    for (int x = 0; x < new_pr.size(); x++) {
                        if (users.getAdmin()) {
                            if (db.Search_product2("AllData", new_pr.get(x).getBarcode()).size() > 0) {
                                Product_class p = db.Search_product2("AllData", new_pr.get(x).getBarcode()).get(0);
                                data.add(new DetailsProductOrder(p, new_pr.get(x).getAmount()));
                            }
                        }else {
                            SEARCH(new_pr.get(x).getBarcode());
                        }
                    }


                    //adapter.setdata(data, getActivity());


                } else {

                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!")
                            .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();

                }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Something went wrong!")
                        .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });
    }

    private void updatepaid(String id) {

        RetrofitClient.getInstance().UpdateOrderPaid(users.getToken(), id,true, getDate() ).enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response) {
                if (response.isSuccessful()) {
                    Notification_Class notification_class = new Notification_Class(users.getAdmin(), "Admin Contact with your order", "orderdetails", o , users.getImage(),oid,new Random().nextInt());

                    mSocket.emit("dbchanged", gson.toJson(notification_class));
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Update Paid")
                            .setContentText("Paid successfully")
                            .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    paid.setClickable(false);
                                    paid.setEnabled(false);
                                    paid.setBackgroundResource(R.drawable.btn_shape_enable);
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();

                } else {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!")
                            .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    paid.setClickable(true);
                                    paid.setEnabled(true);
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();

                }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Something went wrong!")
                        .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                paid.setClickable(true);
                                paid.setEnabled(true);
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();

            }
        });
    }

    private void updatecontact(String id) {

        RetrofitClient.getInstance().UpdateOrderDeleviry(users.getToken(), id, true, getDate()).enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response) {
                if (response.isSuccessful()) {
                    Notification_Class notification_class = new Notification_Class(users.getAdmin(), "Admin Contact with your order", "orderdetails", o , users.getImage(),users.get_id(),new Random().nextInt());
                    mSocket.emit("dbchanged", gson.toJson(notification_class));

                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Update Contacted")
                            .setContentText("Paid successfully")
                            .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    contact.setClickable(false);
                                    contact.setEnabled(false);
                                    contact.setBackgroundResource(R.drawable.btn_shape_enable);
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();

                } else {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!")
                            .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    contact.setClickable(true);
                                    contact.setEnabled(true);
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Something went wrong!")
                        .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                contact.setClickable(true);
                                contact.setEnabled(true);
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });
    }

    private Date getDate() {
        DateFormat dtf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        try {
           date =dtf.parse(String.valueOf(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void SEARCH (String barcode){
        RetrofitClient.getInstance().GETSEARCHRODUCTBARCODE(users.getToken(),barcode).enqueue(new Callback<Product_class>() {
            @Override
            public void onResponse(Call<Product_class> call, Response<Product_class> response) {

                if (response.isSuccessful()){
                        if (response.body() != null){
                            data.add(new DetailsProductOrder(response.body(), new_pr.get(x).getAmount()));
                        }
                    adapter.setdata(data, getActivity());
                   return;
                }else {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!")
                            .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }


            }

            @Override
            public void onFailure(Call<Product_class> call, Throwable t) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Something went wrong!")
                        .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });

    }
}
