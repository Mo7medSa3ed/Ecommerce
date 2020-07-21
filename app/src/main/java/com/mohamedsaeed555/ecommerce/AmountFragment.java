package com.mohamedsaeed555.ecommerce;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.Product_class;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AmountFragment extends Fragment {

    TextInputLayout inputLayoutcode,inputLayoutamount;
    AutoCompleteTextView pcode,pamount,pAmount;
    ImageView bar_image, pimage;
    TextView pname,pprice ,pbrand,pdate;
    Button btn_ok ,btn_search;
    LinearLayout li_message , li_card;
    String collection_name="", barcode="", name="", date="", image="", brand="" , newdate="";
    Double price;
    int amount;
    ArrayList<Product_class> Products = new ArrayList<>();
    OrderAdapter adapter;
    RecyclerView recyclerView;
    Database db;
    boolean test =false;
    SharedPreferences.Editor editor;
    RecyclerView.LayoutManager layoutManager;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db=new Database(getActivity());
        collection_name=getArguments().getString("collection_name","");
        barcode =getArguments().getString("barcode","");
        name =getArguments().getString("name","");
        date =getArguments().getString("date","");
        brand =getArguments().getString("brand","");
        image =getArguments().getString("image","");
        amount =getArguments().getInt("amount",0);
        price =getArguments().getDouble("price",0);
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Database db =new Database(getActivity());
        setHasOptionsMenu(true);
        getActivity().setTitle("Add Amount");

        try {
            if (db.Search_product2("Products",barcode).size()>0){
                test=true;
            }
            Products=db.getAllProducts2("Products");

        }catch (Exception e){e.printStackTrace();}

        inputLayoutcode=view.findViewById(R.id.inputlayout);
        pcode=view.findViewById(R.id.filled_exposed_dropdown);
        btn_ok=view.findViewById(R.id.button);
        btn_search=view.findViewById(R.id.button5);
        li_message=view.findViewById(R.id.li_message);
        li_card=view.findViewById(R.id.li_card);
        pcode.setText(barcode);

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{ Manifest.permission.CAMERA}, 1);
        }
        recyclerView =view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new OrderAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(true);


        if (collection_name.equals("")){
            li_card.setVisibility(View.GONE);
            li_message.setVisibility(View.VISIBLE);
            pcode.setText(barcode);
            btn_ok.setEnabled(false);
            btn_ok.setBackgroundColor(Color.GRAY);

        }else {
            li_card.setVisibility(View.VISIBLE);
            li_message.setVisibility(View.GONE);
            if (!test){
            Product_class productClass = new Product_class(date,amount,barcode,name,price,brand,image,collection_name);
            Products.add(productClass);
            db.insert_product_toAlldata("Products",productClass);
            test=false;
            }
            adapter.setproductdata(Products,getActivity(),true);
            adapter.notifyDataSetChanged();
            btn_ok.setEnabled(true);
            btn_ok.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        pcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 13) {
                    btn_search.setVisibility(View.VISIBLE);
                }else {
                    btn_search.setVisibility(View.GONE);
                }
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Database db = new Database(getActivity());
                Product_class productClass ;
                String bar=pcode.getText().toString().trim();
                if (db.Search_product2("AllData",bar).size() > 0){

                        productClass=db.Search_product2("AllData",bar).get(0);
                        Bundle bundle = new Bundle();
                        bundle.putString("collection_name",productClass.getCollection());
                        bundle.putString("barcode",productClass.getBarcode());
                        bundle.putString("name",productClass.getName());
                        bundle.putString("date",productClass.getDate());
                        bundle.putDouble("price",productClass.getPrice());
                        bundle.putString("brand",productClass.getBrand());
                        bundle.putString("image",productClass.getImage());
                        bundle.putInt("amount",productClass.getAmount());
                        AmountFragment myFragment = new AmountFragment();
                        myFragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, myFragment).addToBackStack(null).commit();



                }else {
                        Bundle bundle = new Bundle();
                        bundle.putString("collection_name","");
                        bundle.putString("barcode","");
                        bundle.putString("name","");
                        bundle.putString("date","");
                        bundle.putDouble("price",0);
                        bundle.putString("brand","");
                        bundle.putString("image","");
                        bundle.putInt("amount",0);
                    AmountFragment myFragment = new AmountFragment();
                        myFragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, myFragment).addToBackStack(null).commit();
                }
            }
        });


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database db =new Database(getActivity());

              /*  if (pcode.getText().toString().equals("")){
                    inputLayoutcode.setError("Please enter product barcode");
                    inputLayoutcode.requestFocus(); return;
                }else if (pamount.getText().toString().equals("")){
                    inputLayoutamount.setError("Please enter product barcode");
                    inputLayoutamount.requestFocus(); return;
                }*/

                    if (Products.size()>0){
                        for (int x=0; x<Products.size(); x++){
                            int count=x;
                            View view = recyclerView.getChildAt(x);
                            pAmount=view.findViewById(R.id.filled_exposed_dropdown3);
                            int amount2 = Integer.parseInt(pAmount.getText().toString().trim());
                            String code =Products.get(x).getBarcode();
                            amount=Products.get(x).getAmount();
                            RetrofitClient.getInstance().UPDATEAMOUNTFORPRODUCT(db.getAllusers().get(0).getToken(),collection_name,code, amount2)
                                    .enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if (!(response.isSuccessful())){
                                                Toast.makeText(getActivity(),response.errorBody().toString(),Toast.LENGTH_SHORT).show();return;
                                            }
                                            int calc =amount+=amount2;
                                            db.update_product3(collection_name,calc,code);
                                            db.update_product3("Products",calc,code);
                                            db.update_product3("AllData",calc,code);

                                            if (count==Products.size()-1){
                                                pcode.setText("");
                                                li_card.setVisibility(View.GONE);
                                                li_message.setVisibility(View.VISIBLE);
                                                btn_ok.setEnabled(true);
                                                btn_ok.setBackgroundColor(Color.GRAY);
                                                Toast.makeText(getActivity(),"Amount Updated Successfully",Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();return;

                                        }
                                    });
                        }

                    }


            }
        });





    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            final int postion = viewHolder.getAdapterPosition();
            if (i == ItemTouchHelper.RIGHT){
                final Product_class productClass = Products.get(postion);
                Products.remove(postion);
                db.Delete_product("Products",String.valueOf(postion+1));
                adapter.notifyItemRemoved(postion);
                Snackbar snackbar = Snackbar.make(recyclerView,productClass.getName()+" is deleted ",Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                db.insert_product_toAlldata("Products",productClass);
                                Products.add(postion,productClass);
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


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.barcode,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.bar){
            Bundle bundle =new Bundle();
            bundle.putString("sale","add");
            ScannerViewForAmount fragment =new ScannerViewForAmount();
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager()
                    .beginTransaction().replace(R.id.cotainers,fragment).addToBackStack(null).commit();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Database db =new Database(getActivity());
        db.Delete_All("Products");
    }
}
