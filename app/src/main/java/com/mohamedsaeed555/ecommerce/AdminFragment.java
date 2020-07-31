package com.mohamedsaeed555.ecommerce;

import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.Product_class;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminFragment extends Fragment {

    public static String activity = "cosmatics";
    public Boolean check = true;
    ArrayList<Product_class> data = new ArrayList<>();
    ListView list;
    ProgressDialog progressDialog;
    SearchView searchView;
    ListAdapter adapter;
    Boolean sort = false;
    LottieAlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        activity = getArguments().getString("activity");


        getActivity().setTitle(activity + " Table");
        Database db = new Database(getActivity());
        list = (ListView) view.findViewById(R.id.list);
        alertDialog = new LottieAlertDialog.Builder(getActivity(), DialogTypes.TYPE_LOADING)
                .setTitle("Loading")
                .setDescription("Please wait until get data")
                .build();
        alertDialog.setCancelable(true);
        alertDialog.show();


        LayoutInflater inflater = getLayoutInflater();
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.tableheader, list, false);
        LinearLayout name = viewGroup.findViewById(R.id.name);
        LinearLayout price = viewGroup.findViewById(R.id.price);
        LinearLayout date = viewGroup.findViewById(R.id.date);
        LinearLayout amount = viewGroup.findViewById(R.id.amount);


        final ImageView img_name = viewGroup.findViewById(R.id.img_name);
        final ImageView img_price = viewGroup.findViewById(R.id.img_price);
        final ImageView img_date = viewGroup.findViewById(R.id.img_date);
        final ImageView img_amount = viewGroup.findViewById(R.id.image_amount);

        list.addHeaderView(viewGroup, null, false);
        adapter = new ListAdapter();
        list.setAdapter(adapter);
        Get_gata();


        //adapter.notifyDataSetChanged();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position--;
                Bundle intent = new Bundle();
                intent.putString("collection_name", activity.toLowerCase());
                intent.putString("barcode", data.get(position).getBarcode());
                intent.putString("date", data.get(position).getDate());
                intent.putString("name", data.get(position).getName());
                intent.putString("price", String.valueOf(data.get(position).getPrice()));
                intent.putString("amount", String.valueOf(data.get(position).getAmount()));
                intent.putString("brand", data.get(position).getBrand());
                intent.putString("image", data.get(position).getImage());
                updateActivity details = new updateActivity();
                details.setArguments(intent);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.cotainers, details).addToBackStack(null).commit();
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sort) {
                    Collections.reverse(data);
                    adapter.setdata(data, getActivity());
                    adapter.notifyDataSetChanged();
                    sort = !sort;
                    img_name.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
                } else {
                    Collections.sort(data, new Comparator<Product_class>() {
                        @Override
                        public int compare(Product_class o1, Product_class o2) {
                            if (o1.getName() != null && o2.getName() != null) {
                                return o1.getName().compareTo(o2.getName());
                            }

                            return 0;
                        }
                    });
                    adapter.setdata(data, getActivity());
                    adapter.notifyDataSetChanged();
                    sort = !sort;
                    img_name.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
                }
            }
        });

        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sort) {
                    Collections.reverse(data);
                    adapter.setdata(data, getActivity());
                    adapter.notifyDataSetChanged();
                    sort = !sort;
                    img_price.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
                } else {
                    Collections.sort(data, new Comparator<Product_class>() {
                        @Override
                        public int compare(Product_class o1, Product_class o2) {
                            return (int) (o1.getPrice() - o2.getPrice());
                        }
                    });
                    adapter.setdata(data, getActivity());
                    adapter.notifyDataSetChanged();
                    sort = !sort;
                    img_price.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
                }

            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sort) {
                    Collections.reverse(data);
                    adapter.setdata(data, getActivity());
                    adapter.notifyDataSetChanged();
                    sort = !sort;
                    img_date.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
                } else {
                    Collections.sort(data, new Comparator<Product_class>() {
                        @Override
                        public int compare(Product_class o1, Product_class o2) {
                            return o1.getDate().compareTo(o2.getDate());
                        }
                    });
                    adapter.setdata(data, getActivity());
                    adapter.notifyDataSetChanged();
                    sort = !sort;
                    img_date.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
                }

            }
        });

        amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sort) {
                    Collections.reverse(data);
                    adapter.setdata(data, getActivity());
                    adapter.notifyDataSetChanged();
                    sort = !sort;
                    img_amount.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
                } else {
                    Collections.sort(data, new Comparator<Product_class>() {
                        @Override
                        public int compare(Product_class o1, Product_class o2) {
                            return o1.getAmount() - o2.getAmount();
                        }
                    });
                    adapter.setdata(data, getActivity());
                    adapter.notifyDataSetChanged();
                    sort = !sort;
                    img_amount.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
                }

            }
        });

    }


    public void Get_gata() {
        final Database db = new Database(getActivity());
        data=db.getAllProductsForAdmin("AllData",activity.toLowerCase());
                Collections.sort(data, new Comparator<Product_class>() {
                    @Override
                    public int compare(Product_class o1, Product_class o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

                adapter.setdata(data, getActivity());
                alertDialog.dismiss();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.search2, menu);
        MenuItem menuItem = menu.findItem(R.id.search2);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search Here !");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.trim().isEmpty()) {
                    check = true;
                    adapter.getFilter().filter(query);
                } else {
                    adapter.getFilter().filter(query);
                    check = false;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().isEmpty()) {
                    check = true;
                    adapter.getFilter().filter(newText);
                } else {
                    adapter.getFilter().filter(newText);
                    check = false;
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


}

