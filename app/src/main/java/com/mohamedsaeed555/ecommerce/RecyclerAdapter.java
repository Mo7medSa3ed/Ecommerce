package com.mohamedsaeed555.ecommerce;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.Product_class;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Product_item> implements Filterable {

    ArrayList<Product_class> product_data = new ArrayList<>();
    ArrayList<Product_class> filterd_data = new ArrayList<>();
    Context context;
    Boolean check = false;
    Boolean fav = false;
    FragmentTransaction transaction;
    FragmentManager fragmentManager;
    private onclick onclick;
    private int lastPosition = -1;


    public RecyclerAdapter(Boolean fav) {
        this.fav = fav;
    }

    public void setdate(ArrayList<Product_class> product_data, Context context, onclick onclick, Boolean check) {
        this.context = context;
        this.onclick = onclick;
        this.check = check;
        for (Product_class p : product_data) {
            this.product_data.add(p);
            this.filterd_data.add(p);
        }
        notifyDataSetChanged();
    }

    public void setdate2(ArrayList<Product_class> product_data, Context context, onclick onclick, Boolean check) {
        this.product_data = product_data;
        this.filterd_data = product_data;
        this.check = check;
        this.onclick = onclick;
        this.context = context;
        notifyDataSetChanged();
    }

    public void setdate3(ArrayList<Product_class> product_data, Context context) {
        this.context = context;
        this.filterd_data = product_data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Product_item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,
                null, false);
        Product_item item = new Product_item(view, onclick);

        return item;
    }


    @Override
    public void onBindViewHolder(@NonNull final Product_item holder, final int position) {
        Database db = new Database(context);
        setAnimation(holder.cardView, position);

        final Product_class details = filterd_data.get(position);
        holder.product_name.setText(details.getName());
        holder.product_price.setText(details.getPrice() + " EGP");
        String url = details.getImage();
        if (url == null) {
            url = "https://www.mezashop.com/image/cache/catalog/Johnson/Johnson%20Baby%20Shampoo%20-%20100ml-600x600-0.jpg";
        }
        Picasso.get().load(url)
                .placeholder(R.drawable.haircode)
                .into(holder.product_image);

        if (fav) {
            holder.menuoption.setVisibility(View.GONE);
        }

        holder.menuoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu menu = new PopupMenu(v.getContext(), v);
                menu.inflate(R.menu.recycle_menu);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                //delete
                                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Are you sure?")
                                        .setContentText("You want to delete this product!")
                                        .setConfirmText("Yes")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                RetrofitClient.getInstance().DELETEPRODUCT(db.getAllusers().get(0).getToken(), HomeFragment.collection_name, details.getBarcode())
                                                        .enqueue(new Callback<Void>() {
                                                            @Override
                                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                                if (response.isSuccessful()) {
                                                                    // db.Delete_product2(HomeFragment.collection_name,details.getBarcode());
                                                                    db.Delete_product2("AllData", details.getBarcode());
                                                                    db.Delete_product2("Cart", details.getBarcode());
                                                                    db.Delete_Fav(details.getBarcode());
                                                                    //Toast.makeText(context,"Product deleted succesfully",Toast.LENGTH_SHORT).show();
                                                                    filterd_data.remove(position);
                                                                    notifyItemRemoved(position);
                                                                    notifyDataSetChanged();

                                                                    sweetAlertDialog.dismissWithAnimation();
                                                                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                                            .setTitleText("Delete Product")
                                                                            .setContentText("product deleted successfully")
                                                                            .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                                                                @Override
                                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                                    sweetAlertDialog.dismissWithAnimation();
                                                                                }
                                                                            })
                                                                            .show();

                                                                } else {
                                                                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
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
                                                            public void onFailure(Call<Void> call, Throwable t) {
                                                                LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(context, DialogTypes.TYPE_ERROR)
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
                                                                alertDialog.show();
                                                            }
                                                        });
                                            }
                                        })
                                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                            }
                                        })
                                        .show();

                                return true;

                            case R.id.update:
                                //update
                                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                Bundle intent = new Bundle();
                                intent.putString("collection_name", HomeFragment.collection_name);
                                intent.putString("barcode", details.getBarcode());
                                intent.putString("date", details.getDate());
                                intent.putString("name", details.getName());
                                intent.putString("price", String.valueOf(details.getPrice()));
                                intent.putString("amount", String.valueOf(details.getAmount()));
                                intent.putString("brand", details.getBrand());
                                intent.putString("image", details.getImage());
                                updateActivity myFragment = new updateActivity();
                                myFragment.setArguments(intent);
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, myFragment).addToBackStack(null).commit();

                                return true;
                            default:
                                return false;
                        }

                    }
                });
                menu.show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return filterd_data.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Database db = new Database(context);
                final ArrayList<Product_class> result_list = new ArrayList<>();
                String searchstring = constraint.toString().toLowerCase().trim();
                if (searchstring.isEmpty()) {
                    // filterd_data.clear();
                    filterd_data = product_data;
                    // result_list.addAll(product_data);
                } else {

                    if (check) {
                        for (int x = 0; x < product_data.size(); x++) {
                            if (product_data.get(x).getName().contains(searchstring)) {
                                result_list.add(product_data.get(x));
                            }
                        }
                        filterd_data = result_list;

                    } else {
                        RetrofitClient.getInstance().GETSEARCHRODUCTNAME(db.getAllusers().get(0).getToken(), HomeFragment.collection_name.toLowerCase(), searchstring).enqueue(new Callback<List<Product_class>>() {
                            @Override
                            public void onResponse(Call<List<Product_class>> call, Response<List<Product_class>> response) {
                                if (!(response.isSuccessful())) {
                                    Toast.makeText(context, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                                }
                                for (Product_class p : response.body()) {
                                    result_list.add(p);
                                }
                                filterd_data = result_list;

                            }

                            @Override
                            public void onFailure(Call<List<Product_class>> call, Throwable t) {
                                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterd_data;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //filterd_data.clear();
                filterd_data = (ArrayList<Product_class>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated

        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public interface onclick {
        void onclick(int position);
    }

    class Product_item extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView product_name, product_price, menuoption;
        ImageView product_image;
        CardView cardView;
        onclick onclick;


        public Product_item(@NonNull View itemView, onclick onclick) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card);
            product_image = itemView.findViewById(R.id.imageView2);
            product_name = itemView.findViewById(R.id.textView2);
            menuoption = itemView.findViewById(R.id.textView5);
            product_price = itemView.findViewById(R.id.textView3);
            this.onclick = onclick;
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Bundle bundle = new Bundle();
            if (check) {
                bundle.putString("collection_name", filterd_data.get(getAdapterPosition()).getCollection());

            } else {
                bundle.putString("collection_name", HomeFragment.collection_name);
            }
            bundle.putString("barcode", filterd_data.get(getAdapterPosition()).getBarcode());
            bundle.putString("name", filterd_data.get(getAdapterPosition()).getName());
            bundle.putString("date", filterd_data.get(getAdapterPosition()).getDate());
            bundle.putDouble("price", filterd_data.get(getAdapterPosition()).getPrice());
            bundle.putString("brand", filterd_data.get(getAdapterPosition()).getBrand());
            bundle.putString("image", filterd_data.get(getAdapterPosition()).getImage());
            bundle.putInt("amount", filterd_data.get(getAdapterPosition()).getAmount());
            DetailsProductActivity myFragment = new DetailsProductActivity();
            myFragment.setArguments(bundle);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, myFragment).addToBackStack(null).commit();
        }
    }


}