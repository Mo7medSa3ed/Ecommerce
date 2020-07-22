package com.mohamedsaeed555.ecommerce;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.mohamedsaeed555.MyDataBase.Orders;
import com.mohamedsaeed555.MyDataBase.Product_class;

import java.util.ArrayList;

public class OrdersAdapter extends BaseAdapter implements Filterable {

    ArrayList<Orders> orders ;
    ArrayList<Orders> filterorders ;
    Context context;
    LayoutInflater inflater;
    Double total=0.0;

    public OrdersAdapter() {
    }

    public OrdersAdapter(ArrayList<Orders> orders, Context context) {
        this.orders = orders;
        this.filterorders=orders;
        this.context = context;
    }

    public void setdata(ArrayList<Orders> orders, Context context) {
        this.orders = orders;
        this.filterorders=orders;
        this.context = context;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return filterorders.size();
    }

    @Override
    public Object getItem(int position) {
        return filterorders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v==null){
            inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.main_order,parent,false);
        }

        TextView o_name = v.findViewById(R.id.textView14);
        TextView o_address = v.findViewById(R.id.textView42);
        TextView o_price = v.findViewById(R.id.textView41);
        ImageView img = v.findViewById(R.id.imageView11);
        LinearLayout ly = v.findViewById(R.id.expa);
        ImageView arrow = v.findViewById(R.id.imageView15);
        ExpandableRelativeLayout ex = v.findViewById(R.id.expandableLayout1);

        if (ex.isExpanded()){ex.collapse();
            arrow.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);}

        o_name.setText(filterorders.get(position).getBy().getName());
        o_address.setText(filterorders.get(position).getBy().getCity());

        if (filterorders.get(position).getTotal().size()>1) {
            for (int x = 0; x < filterorders.get(position).getTotal().size(); x++) {
                total += filterorders.get(position).getTotal().get(x);
            }
        }else {
            total= filterorders.get(position).getTotal().get(0);
        }

        o_price.setText(String.valueOf(total)+" EGP");

        if (filterorders.get(position).getPaid()){
            img.setImageResource(R.drawable.ic_check_black_24dp);
        }else {
            img.setImageResource(R.drawable.ic_clear_black_24dp);
        }

        ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ex.isExpanded()){
                    ex.collapse();
                    arrow.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                }else {
                    ex.expand();
                    arrow.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                }
            }
        });




        return v;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
               ArrayList<Orders> result = new ArrayList<>();
               String search_word = constraint.toString().toLowerCase().trim();
               if (search_word.isEmpty()){
                   filterorders = orders;
               }else {
                   for (int x=0; x<orders.size(); x++){
                       if (orders.get(x).getBy().getName().toLowerCase().trim().contains(search_word)){
                           result.add(orders.get(x));
                       }
                   }

                   filterorders=result;
               }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterorders;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterorders = (ArrayList<Orders>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
