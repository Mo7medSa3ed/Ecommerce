package com.mohamedsaeed555.ecommerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohamedsaeed555.MyDataBase.DetailsProductOrder;
import com.mohamedsaeed555.MyDataBase.Orders;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailsOrderAdapter extends BaseAdapter {
    ArrayList<DetailsProductOrder> list ;
    Context context;
    LayoutInflater inflater;

    public DetailsOrderAdapter() {
    }

    public DetailsOrderAdapter(ArrayList<DetailsProductOrder> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void  setdata(ArrayList<DetailsProductOrder> list, Context context){
        this.list=list;
        this.context=context;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v =convertView;
        if (v==null){
            inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.order_product,parent,false);
        }

        TextView pname = v.findViewById(R.id.textView27);
        TextView pamount = v.findViewById(R.id.textView31);
        ImageView pimg = v.findViewById(R.id.imageView12);

        pname.setText(list.get(position).getProduct().getName());
        pamount.setText(String.valueOf(list.get(position).getAmount()));
        Picasso.get().load(list.get(position).getProduct().getImage()).placeholder(R.drawable.haircode).into(pimg);


        return v;
    }
}
