package com.mohamedsaeed555.ecommerce;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mohamedsaeed555.MyDataBase.Product_class;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Product_order> {

    ArrayList<Product_class> product_data = new ArrayList<>();
    Context context;
    Boolean check = false;

    private int lastPosition = -1;

    public void setproductdata(ArrayList<Product_class> product_data, Context context, boolean check) {
        this.product_data = product_data;
        this.context = context;
        this.check = check;

        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public Product_order onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_item,
                viewGroup, false);
        Product_order item = new Product_order(view);

        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull final Product_order holder, int position) {
        setAnimation(holder.cardView, position);
        final Product_class details = product_data.get(position);
        Picasso.get().load(details.getImage()).placeholder(R.drawable.haircode).into(holder.product_image);
        holder.product_name.setText(details.getName());
        holder.product_price.setText(String.valueOf(details.getPrice()));
        holder.final_price.setText(String.valueOf(details.getPrice()));
        if (check) {

            holder.text_amount.setText("0");
            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int amount = Integer.parseInt(String.valueOf(holder.text_amount.getText()));
                    double price = Double.parseDouble(String.valueOf(holder.product_price.getText()));
                    amount++;
                    if (amount > 0) {
                        holder.text_amount.setText(String.valueOf(amount));
                        holder.final_price.setText(String.valueOf(amount * price));
                    }
                }
            });

        } else {
            holder.text_amount.setText("1");

            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int amount = Integer.parseInt(String.valueOf(holder.text_amount.getText()));
                    double price = Double.parseDouble(String.valueOf(holder.product_price.getText()));
                    amount++;
                    if (amount > 0 && amount <= product_data.get(position).getAmount()) {
                        holder.text_amount.setText(String.valueOf(amount));
                        holder.final_price.setText(String.valueOf(amount * price));
                    }
                }
            });

            holder.text_amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!(s.toString().isEmpty()) && holder.text_amount.isFocused())
                        holder.calc.setVisibility(View.VISIBLE);
                }
            });

            holder.calc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.text_amount.getText().toString().isEmpty()) {
                        holder.text_amount.setText("1");
                        holder.calc.setVisibility(View.GONE);
                        holder.text_amount.setFocusable(false);
                    } else {
                        int amount = Integer.parseInt(String.valueOf(holder.text_amount.getText()));
                        double price = Double.parseDouble(String.valueOf(holder.product_price.getText()));
                        if (amount > 0) {
                            holder.text_amount.setText(String.valueOf(amount));
                            holder.final_price.setText(String.valueOf(amount * price));
                            holder.calc.setVisibility(View.GONE);
                            holder.text_amount.setFocusable(false);
                        }
                    }
                }
            });

        }
        holder.substract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount = 1;
                amount = Integer.parseInt(String.valueOf(holder.text_amount.getText()));
                double price = Double.parseDouble(String.valueOf(holder.product_price.getText()));
                amount--;
                if (amount > 0) {
                    holder.text_amount.setText(String.valueOf(amount));
                    holder.final_price.setText(String.valueOf(amount * price));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return product_data.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated

        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    class Product_order extends RecyclerView.ViewHolder {

        TextView product_name, product_price, final_price;
        ImageView product_image, plus, substract;
        AutoCompleteTextView text_amount;
        CardView cardView;
        Button calc;

        public Product_order(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card2);
            product_image = itemView.findViewById(R.id.imageView12);
            product_name = itemView.findViewById(R.id.textView27);
            product_price = itemView.findViewById(R.id.textView30);
            plus = itemView.findViewById(R.id.imageButton2);
            substract = itemView.findViewById(R.id.imageButton);
            final_price = itemView.findViewById(R.id.textView28);
            text_amount = itemView.findViewById(R.id.filled_exposed_dropdown3);
            calc = itemView.findViewById(R.id.button3);
        }
    }

}
