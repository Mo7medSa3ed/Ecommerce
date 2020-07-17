package com.mohamedsaeed555.ecommerce;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.mohamedsaeed555.MyDataBase.Product_class;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListAdapter extends BaseAdapter implements Filterable {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Product_class> arrayList = new ArrayList<>();
    ArrayList<Product_class> filterd_data=new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");

    public void setdata(ArrayList<Product_class> arrayList , Context context){
        this.context=context;
        this.arrayList=arrayList;
        this.filterd_data=arrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return filterd_data.size();
    }

    @Override
    public Object getItem(int position) {
        return filterd_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v==null){
           layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           v=layoutInflater.inflate(R.layout.list_item,null,false);
        }

        TextView Name =(TextView)v.findViewById(R.id.textView4);
        TextView Price=(TextView)v.findViewById(R.id.textView24);
        TextView date=(TextView)v.findViewById(R.id.textView9);
        TextView amount=(TextView)v.findViewById(R.id.textView7);

            Name.setText(filterd_data.get(position).getName());
            Price.setText(String.valueOf(filterd_data.get(position).getPrice()));
            amount.setText(String.valueOf(filterd_data.get(position).getAmount()));
            if (filterd_data.get(position).getDate()==null || filterd_data.get(position).getDate().isEmpty() || filterd_data.get(position).getDate()=="") {
                date.setText(filterd_data.get(position).getDate());
            }else {
               if (filterd_data.get(position).getDate().toCharArray().length>=10){
                    String d =filterd_data.get(position).getDate().substring(0,10);
                   try {
                       date.setText(sdf.format(sdf2.parse(d)));
                   } catch (ParseException e) {
                       e.printStackTrace();
                   }
               }else {
                   try {
                       date.setText(sdf.format(sdf2.parse(filterd_data.get(position).getDate())));
                   } catch (ParseException e) {
                       e.printStackTrace();
                   }

               }

            }


        return v;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final ArrayList<Product_class> result_list = new ArrayList<>();
                String searchstring = constraint.toString().toLowerCase().trim();
                if (searchstring.isEmpty()) {
                    // filterd_data.clear();
                    filterd_data=arrayList;
                    // result_list.addAll(product_data);
                } else {
                    for (int x=0; x<arrayList.size();x++){
                        if (arrayList.get(x).getName().toLowerCase().trim().contains(searchstring)){
                            result_list.add(arrayList.get(x));
                        }
                    }
                    filterd_data=result_list;

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
}
