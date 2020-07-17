package com.mohamedsaeed555.ecommerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.mohamedsaeed555.MyDataBase.Product_class;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    ArrayList<Product_class> list ;
    LayoutInflater layoutInflater;
    Context context;

    public ViewPagerAdapter(ArrayList<Product_class> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.order_item,container,false);

        ImageView img =view.findViewById(R.id.imageView2);
        TextView name = view.findViewById(R.id.textView2);
        TextView price = view.findViewById(R.id.textView3);
        TextView edit = view.findViewById(R.id.textView5);
        CardView card= view.findViewById(R.id.card2);

        final Product_class details = list.get(position);

        Picasso.get().load(details.getImage())
                .placeholder(R.drawable.haircode)
                .into(img);

        name.setText(details.getName());
        price.setText(String.valueOf(details.getPrice())+" EGP");

        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
