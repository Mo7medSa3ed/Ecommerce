package com.mohamedsaeed555.ecommerce;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.Product_class;

import java.util.ArrayList;


public class FavouriteFragment extends Fragment implements RecyclerAdapter.onclick {

    ArrayList<Product_class> arrayList = new ArrayList<>();
    String barcode;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    SearchView searchView;

    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle("Favourite");
        Database db = new Database(getActivity());
        recyclerView = view.findViewById(R.id.recycler_fav);
        int size = db.GETALLFAV().size();
        if (size > 0) {
            arrayList.clear();
            for (int x = 0; x < size; x++) {
                barcode = db.GETALLFAV().get(x);
                arrayList.add(db.Search_product2("AllData", barcode).get(0));
            }
        }


        int Colums_Count = calculateNoOfColumns(getActivity(), 180);

        layoutManager = new GridLayoutManager(getActivity(), Colums_Count);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(true);
        recyclerView.setAdapter(adapter);

        adapter.setdate2(arrayList, getActivity(), FavouriteFragment.this, true);


    }

    @Override
    public void onclick(int position) {

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
                    //check=true;
                    adapter.getFilter().filter(query);
                } else {
                    adapter.getFilter().filter(query);
                    // check=false;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().isEmpty()) {
                    //check=true;
                    adapter.getFilter().filter(newText);
                } else {
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
        if (id == R.id.search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
