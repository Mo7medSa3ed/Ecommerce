package com.mohamedsaeed555.ecommerce;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.ObjectProduct;
import com.mohamedsaeed555.MyDataBase.Orders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrdersFragment extends Fragment {

    ListView list;
    OrdersAdapter adapter;
    ArrayList<Orders> orders = new ArrayList<>();
    ArrayList<Orders> filter_orders = new ArrayList<>();
    ChipGroup chipGroup;
    Database database;
    SearchView searchView;
    Double total=0.0;
    Gson gson =new Gson();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        database=new Database(getActivity());
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        list = view.findViewById(R.id.orderlist);
        chipGroup = view.findViewById(R.id.chipgroup2);

        adapter= new OrdersAdapter(orders,getActivity());
        list.setAdapter(adapter);

        GETALLORDERS();

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip = (group).findViewById(checkedId);
                if (chip != null) {
                    if (checkedId == R.id.chip_cosmatics) {
                        filter_orders.clear();
                        for (int x =0;x<orders.size(); x++){
                            if (orders.get(x).getPaid()){
                                filter_orders.add(orders.get(x));
                            }
                        }
                        adapter.setdata(filter_orders,getActivity());

                    } else if (checkedId == R.id.chip_Medical) {
                        filter_orders.clear();
                        for (int x =0;x<orders.size(); x++){
                            if (orders.get(x).getDelivery()){
                                filter_orders.add(orders.get(x));
                            }
                        }
                        adapter.setdata(filter_orders,getActivity());

                    } else if (checkedId == R.id.chip_Makups) {
                        Collections.sort(orders, new Comparator<Orders>() {
                            @Override
                            public int compare(Orders orders, Orders t1) {
                                return orders.getBy().getCity().compareTo(t1.getBy().getCity());
                            }
                        });

                        adapter.setdata(orders,getActivity());

                    } else if (checkedId == R.id.chip_papers) {
                        Collections.sort(orders, new Comparator<Orders>() {
                            @Override
                            public int compare(Orders orders, Orders t1) {
                                return (int) (orders.getTotal().get(0)-t1.getTotal().get(0));
                            }
                        });

                        adapter.setdata(orders,getActivity());
                    }
                }

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("order",gson.toJson(orders.get(position)));
                Orders_Details frag = new Orders_Details();
                frag.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.cotainers,frag).addToBackStack(null).commit();

            }
        });


}

    private void GETALLORDERS(){
        RetrofitClient.getInstance().GetAllOrder().enqueue(new Callback<List<Orders>>() {
            @Override
            public void onResponse(Call<List<Orders>> call, Response<List<Orders>> response) {
                if (response.isSuccessful()){
                    orders.clear();
                    int size = response.body().size();
                    for (int x=0; x<size; x++){

                        response.body().get(x).setTotal(Sum(response.body().get(x).getTotal()));
                        orders.add(response.body().get(x));
                    }
                    adapter.setdata(orders,getActivity());


                }else {
                    Toast.makeText(getActivity(),String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Orders>> call, Throwable t) {
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search2, menu);
        MenuItem menuItem = menu.findItem(R.id.search2);
        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView)   menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search Here !");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.trim().isEmpty()){
                    //check=true;
                    adapter.getFilter().filter(query);
                }else {
                    adapter.getFilter().filter(query);
                    //check=false;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().isEmpty()){
                    //check=true;
                    adapter.getFilter().filter(newText);
                }else {
                    adapter.getFilter().filter(newText);
                    //check=false;
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search2) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Double> Sum (ArrayList<Double> arr){
        ArrayList<Double> tot =new ArrayList<>();
        Double d =0.0;
        for (int x =0; x<arr.size(); x++){
            d+=arr.get(x);
        }
        tot.add(d);
        return tot ;
    }
}
