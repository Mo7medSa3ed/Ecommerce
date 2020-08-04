package com.mohamedsaeed555.ecommerce;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.ObjectProduct;
import com.mohamedsaeed555.MyDataBase.Poset_Orders;
import com.mohamedsaeed555.MyDataBase.Product_class;
import com.mohamedsaeed555.MyDataBase.Users;
import com.mohamedsaeed555.Notification.Notification_Class;

import org.jetbrains.annotations.NotNull;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Order_List extends Fragment {
    ArrayList<ObjectProduct> products = new ArrayList<>();
    RecyclerView recyclerView;
    OrderAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    Database db;
    ArrayList<Product_class> orders = new ArrayList<>();
    AutoCompleteTextView pamount;
    Users users;
    Poset_Orders.User user;
    TextView fprice;
    ArrayList<Double> t = new ArrayList<>();
    Boolean check = false;
    Gson gson = new Gson();
    LottieAlertDialog alertDialog;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            final int postion = viewHolder.getAdapterPosition();
            if (i == ItemTouchHelper.RIGHT) {
                final Product_class productClass = orders.get(postion);
                orders.remove(postion);
                db.Delete_product("Cart", String.valueOf(postion + 1));
                adapter.notifyItemRemoved(postion);
                Snackbar snackbar = Snackbar.make(recyclerView, productClass.getName() + " is deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                db.insert_product_tocart("Cart", productClass);
                                orders.add(postion, productClass);
                                adapter.notifyItemInserted(postion);
                            }
                        });
                snackbar.getView().setBackgroundColor(Color.WHITE);
                snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }
        }
    };
    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("https://newaccsys.herokuapp.com");
        } catch (URISyntaxException e) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db = new Database(getActivity());
        orders = db.getAllProducts("Cart");
        users = db.getAllusers().get(0);
        user = new Poset_Orders.User(users.getName(), users.getEmail(), users.getTel(), users.getAdress(), users.getCity());
        return inflater.inflate(R.layout.fragment_order__list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Cart");

        if (orders.size()==0){
            new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setCustomImage(R.drawable.ic_baseline_shopping_cart_24)
                    .setTitleText("Cart")
                    .setContentText("Cart is Empty")
                    .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.cotainers, new HomeFragment()).addToBackStack(null).commit();
                        }
                    })
                    .show();
        }


        Button btn = view.findViewById(R.id.order_btn);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new OrderAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setproductdata(orders, getActivity(), false);
        adapter.notifyDataSetChanged();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Users users = db.getAllusers().get(0);
                if (users.getTel() == null || users.getCity() == null || users.getAdress() == null) {
                    alertDialog = new LottieAlertDialog.Builder(getActivity(), DialogTypes.TYPE_QUESTION)
                            .setTitle("Complete Profile Info")
                            .setDescription("Do you want to update profile information ?")
                            .setPositiveText("yes")
                            .setNegativeText("No")
                            .setPositiveButtonColor(Color.parseColor("#f44242"))
                            .setPositiveTextColor(Color.parseColor("#0a0906"))
                            .setNegativeButtonColor(Color.parseColor("#ffbb00"))
                            .setNegativeTextColor(Color.parseColor("#0a0906"))
                            .setPositiveListener(new ClickListener() {
                                @Override
                                public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                                    alertDialog.dismiss();
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.cotainers, new UserSetting())
                                            .addToBackStack(null).commit();
                                }
                            })
                            .setNegativeListener(new ClickListener() {
                                @Override
                                public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                                    alertDialog.dismiss();
                                }
                            })
                            .build();
                    alertDialog.show();


                } else {
                    if (orders.size() > 0) {
                        Double total = 0.0;
                        for (int x = 0; x < orders.size(); x++) {
                            View v = recyclerView.getChildAt(x);
                            pamount = v.findViewById(R.id.filled_exposed_dropdown3);
                            fprice = v.findViewById(R.id.textView30);
                            Double t = Double.parseDouble(fprice.getText().toString());
                            int a = Integer.parseInt(pamount.getText().toString().trim());
                            total += (t * a);
                            products.add(new ObjectProduct(orders.get(x).getBarcode(), a));
                        }
                        t.add(total);
                        Poset_Orders order = new Poset_Orders(user, products, t);
                        Double finalTotal = total;
                        RetrofitClient.getInstance().PostOrder(users.getToken(), order).enqueue(new Callback<Poset_Orders>() {
                            @Override
                            public void onResponse(Call<Poset_Orders> call, Response<Poset_Orders> response) {
                                if (response.isSuccessful()) {
                                    check = true;
                                    Notification_Class notification_class = new Notification_Class(users.getAdmin(), "New Order From User", "orderdetails", response.body() , users.getImage(),users.get_id(),new Random().nextInt());
                                    mSocket.emit("dbchanged", gson.toJson(notification_class));


                                    for (int x = 0; x < orders.size(); x++) {
                                        if (check == true) {
                                            View v = recyclerView.getChildAt(x);
                                            pamount = v.findViewById(R.id.filled_exposed_dropdown3);
                                            int a = Integer.parseInt(pamount.getText().toString().trim()) * -1;
                                            RetrofitClient.getInstance().UPDATEAMOUNTFORPRODUCT(users.getToken(), "cosmatics", orders.get(x).getBarcode(), a).enqueue(new Callback<Void>() {
                                                @Override
                                                public void onResponse(Call<Void> call, Response<Void> response) {

                                                }

                                                @Override
                                                public void onFailure(Call<Void> call, Throwable t) {

                                                }
                                            });
                                        }
                                    }

                                    check=false;
                                    db.Delete_All("Cart");
                                    orders.clear();
                                    adapter.setproductdata(orders, getActivity(), false);
                                    adapter.notifyDataSetChanged();

                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Total is "+String.valueOf(finalTotal)+" EGP")
                                            .setContentText("Order added successfully")
                                            .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismissWithAnimation();
                                                    getActivity().getSupportFragmentManager().beginTransaction()
                                                            .replace(R.id.cotainers, new HomeFragment()).addToBackStack(null).commit();
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
                                                    sweetAlertDialog.dismissWithAnimation();
                                                    check = false;
                                                }
                                            })
                                            .show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Poset_Orders> call, Throwable t) {
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Something went wrong!")
                                        .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                                check = false;
                                            }
                                        })
                                        .show();

                            }
                        });

                    }
                }
            }
        });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

}
   /*LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(getActivity(), DialogTypes.TYPE_ERROR)
                        .setTitle("Error")
                        .setDescription("Some error has happened.")
                        .setPositiveText("Okay")
                       .setPositiveListener(new ClickListener() {
                           @Override
                           public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                               lottieAlertDialog.dismiss();
                           }
                       })
                        .build();
                alertDialog.setCancelable(false);
                alertDialog.show();*/

                 /*new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                         .setTitleText("Oops...")
                         .setContentText("Something went wrong!")
                         .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
@Override
public void onClick(SweetAlertDialog sweetAlertDialog) {
        sweetAlertDialog.dismissWithAnimation();
        }
        })
        .show();*/