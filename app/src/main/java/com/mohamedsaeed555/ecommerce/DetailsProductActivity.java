package com.mohamedsaeed555.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.gson.Gson;
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.Product_class;
import com.mohamedsaeed555.MyDataBase.Users;
import com.mohamedsaeed555.Notification.Notification_Class;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsProductActivity extends Fragment {
    ImageView img ,img_fav;
    TextView pname, pprice, pdate, pbrand, pamount, alsolike ,smsCountTxt;
    String collection_name, barcode, name, date, image, brand, Newdate;
    Double price;
    int amount;
    CardView cardView;
    LinearLayout linearLayout , lay_search,lay_fav;
    ArrayList<Product_class> similar = new ArrayList<>();

    SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    FloatingActionButton actionButton;
    ArrayList<Product_class> product_search = new ArrayList<>();
    Database db;
    Boolean test=true;
    int pendingSMSCount = 0;
    int size=0;
    Gson gson = new Gson();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       try {

           collection_name = getArguments().getString("collection_name","");
           barcode = getArguments().getString("barcode");
           name = getArguments().getString("name");
           date = getArguments().getString("date");
           brand = getArguments().getString("brand");
           image = getArguments().getString("image");
           amount = getArguments().getInt("amount");
           price = getArguments().getDouble("price");

           if (collection_name == null || collection_name.equals("")) {
               Toast.makeText(getActivity(),"Dsad",Toast.LENGTH_LONG).show();
               collection_name = getArguments().getString("col", "");
               Product_class c = gson.fromJson(getArguments().getString("pd"), Product_class.class);
               barcode = c.getBarcode();
               name = c.getName();
               date = c.getDate();
               brand = c.getBrand();
               image = c.getImage();
               amount = c.getAmount();
               price = c.getPrice();
           }
       }catch (Exception e){e.printStackTrace();}

        return inflater.inflate(R.layout.activity_details_product,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new Database(getActivity());
        getActivity().setTitle("Product Details");
        size=db.Search_product("Cart",barcode).size();
        pendingSMSCount=db.getAllProducts("Cart").size();
        setHasOptionsMenu(true);
        img = view.findViewById(R.id.imageView9);
        pname = view.findViewById(R.id.textView16);
        pprice = view.findViewById(R.id.textView23);
        pbrand = view.findViewById(R.id.textView21);
        pdate = view.findViewById(R.id.textView17);
        pamount = view.findViewById(R.id.textView26);
        alsolike = view.findViewById(R.id.textView32);
        cardView = view .findViewById(R.id.card_item);
        alsolike.setVisibility(View.GONE);
        actionButton=view.findViewById(R.id.floatingActionButton2);
        linearLayout = view.findViewById(R.id.size);
        lay_search = view.findViewById(R.id.lay_search);
        lay_fav = view.findViewById(R.id.lay_fav);
        img_fav=view.findViewById(R.id.imageView10);



        if (size>0){
            test=false;
        }

        if (db.Search_fav(barcode).size()>0){
            img_fav.setImageResource(R.drawable.ic_favorite_black_24dp);
        }else {
            img_fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }

        Picasso.get().load(image).placeholder(R.drawable.haircode).into(img);
        pname.setText(name);
        pamount.setText(String.valueOf(amount));
        pprice.setText(String.valueOf(price)+" EGP");
        pbrand.setText(brand);
        try {

            if (date.toCharArray().length >= 10) {
                String d = date.substring(0, 10);
                try {
                    Newdate = sdf.format(sdf2.parse(d));
                    pdate.setText(Newdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Newdate = sdf.format(sdf2.parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                pdate.setText(Newdate);
            }
        }catch (Exception e){e.printStackTrace();}

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView = view.findViewById(R.id.recy);
        recyclerView.setLayoutManager(manager);
        adapter = new RecyclerAdapter(false);
        recyclerView.setAdapter(adapter);


        GETONEPRODUCTDETAILS(collection_name,barcode);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (test && size==0){

                    Product_class productClass = new Product_class(Newdate,amount,barcode,name,price,brand,image);
                    db.insert_product_tocart("Cart",productClass);
                    actionButton.setClickable(false);
                    pendingSMSCount=db.getAllProducts("Cart").size();
                    setupBadge();
                    test=true;
                    Toast.makeText(getActivity(),"Product Added Successfully",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getActivity(),"Product Added Previously",Toast.LENGTH_LONG).show();
                }

            }
        });


        lay_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY,barcode); // query contains search string
                startActivity(intent);
            }
        });


        lay_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database db =new Database(getActivity());
                ArrayList<String> fav =new ArrayList<>();
                String name , tel,city,image,address,email,pass,_id,goid,fid;
                Boolean admin ,superadmin;

                if (db.Search_fav(barcode).size()>0){
                    fav.clear();
                }else {
                    fav.add(barcode);
                }
                 Users users = db.getAllusers().get(0);
                _id = users.get_id();
                RetrofitClient.getInstance().UpdateUser_Fav(_id,fav).enqueue(new Callback<Users>() {
                    @Override
                    public void onResponse(Call<Users> call, Response<Users> response) {
                        if (response.isSuccessful()){
                            if (fav.size()!=0){
                                db.insert_fav(barcode);
                            }else {
                                db.Delete_Fav(barcode);
                            }
                            fav.clear();

                            if (db.Search_fav(barcode).size()>0){
                                img_fav.setImageResource(R.drawable.ic_favorite_black_24dp);
                            }else {
                                img_fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            }

                        }else {
                            Toast.makeText(getActivity(),String.valueOf(response.code()),Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Users> call, Throwable t) {
                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });


    }

    private void GETONEPRODUCTDETAILS(final String colection_name , final String barcode){
        RetrofitClient.getInstance().GETONEPRODUCTDETAILS(colection_name,barcode).enqueue(new Callback<One_product_class>() {
            @Override
            public void onResponse(Call<One_product_class> call, Response<One_product_class> response) {
                if (!(response.isSuccessful())){
                    Toast.makeText(getActivity(),String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                }
                    similar.clear();
                    try {
                        if (!(response.body().getSamilier().size() == 0)) {
                            for (Product_class p : response.body().getSamilier()) {
                                if (!(p.getBarcode().equals(barcode)))
                                    similar.add(p);
                            }

                            adapter.setdate3(similar, getActivity());
                        }
                    }catch (Exception e){e.printStackTrace();}

                    if (similar.size()==0 || similar==null)
                        alsolike.setVisibility(View.VISIBLE);








               // Toast.makeText(getActivity(),String.valueOf(similar.size()),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<One_product_class> call, Throwable t) {
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.cart,menu);
        final MenuItem menuItem = menu.findItem(R.id.cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        smsCountTxt = (TextView) actionView.findViewById(R.id.notification_badge);
        setupBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
         super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id2 = item.getItemId();
        if (id2 == R.id.cart) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.cotainers,new Order_List()).addToBackStack(null).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {

        if (smsCountTxt != null) {
            if (pendingSMSCount == 0) {
                if (smsCountTxt.getVisibility() != View.GONE) {
                    smsCountTxt.setVisibility(View.GONE);
                }
            } else {
                smsCountTxt.setText(String.valueOf(pendingSMSCount));
                if (smsCountTxt.getVisibility() != View.VISIBLE) {
                    smsCountTxt.setVisibility(View.VISIBLE);
                }
            }
        }
    }

}

