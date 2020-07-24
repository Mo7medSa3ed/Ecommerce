package com.mohamedsaeed555.ecommerce;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.Product_class;
import com.mohamedsaeed555.MyDataBase.Retrofit_class_data;
import com.mohamedsaeed555.MyDataBase.Users;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements RecyclerAdapter.onclick {

    public static String collection_name = "cosmatics";
    public Boolean check = true;
    //GoogleSignInClient mGoogleSignInClient;
    // GoogleSignInAccount acct;
    ArrayList<Product_class> data2 = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    SearchView searchView;
    RecyclerView.LayoutManager layoutManager;
    SharedPreferences.Editor editor;
    ChipGroup chipGroup;
    Database db;
    int page = 1;
    Boolean isloading = true;
    ProgressBar progressBar;
    FloatingActionButton moveup;
    SwipeRefreshLayout swipeRefreshLayout;
    int checkid;
    SharedPreferences shp;
    Users users;
    TextView smsCountTxt, drawer_text;
    int cart_size = 0;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;
    private int pastVisibleItems = 0;

    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = new Database(getActivity());
        users = db.getAllusers().get(0);
        cart_size = db.getAllProducts("Cart").size();
        return inflater.inflate(R.layout.activity_home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        getActivity().setTitle("Ecommerce");

        recyclerView = view.findViewById(R.id.recycler);
        chipGroup = (ChipGroup) view.findViewById(R.id.chipgroup);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        moveup = view.findViewById(R.id.floatingActionButton);
        swipeRefreshLayout = view.findViewById(R.id.SwipeRefreshLayout);

        shp = getActivity().getSharedPreferences("Checked", getActivity().MODE_PRIVATE);
        editor = getActivity().getSharedPreferences("Checked", getActivity().MODE_PRIVATE).edit();

        checkid = shp.getInt("id", R.id.chip_cosmatics);
        collection_name = shp.getString("collection", "cosmatics");

        if (checkid == 0) {
            checkid = R.id.chip_cosmatics;
        } else {
            chipGroup.check(checkid);
        }

        int Colums_Count = calculateNoOfColumns(getActivity(), 180);

        layoutManager = new GridLayoutManager(getActivity(), Colums_Count);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        if (users.getToken() == null || users.getAdmin() == false) {
            adapter = new RecyclerAdapter(true);
        } else {
            adapter = new RecyclerAdapter(false);
        }
        recyclerView.setAdapter(adapter);

        // Toast.makeText(getActivity(),String.valueOf(checkid),Toast.LENGTH_LONG).show();

        if (chipGroup.getCheckedChipId() == R.id.chip_cosmatics) {
            Getcosmatics2(collection_name, String.valueOf(page));
        }

        moveup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                Getcosmatics2(collection_name, String.valueOf(page));
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        /* if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
        }*/


        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip = (group).findViewById(checkedId);
                if (chip != null) {
                    if (checkedId == R.id.chip_cosmatics) {
                        page = 1;
                        collection_name = "cosmatics";
                        Getcosmatics2(collection_name, String.valueOf(page));
                        return;
                    } else if (checkedId == R.id.chip_Medical) {
                        page = 1;
                        collection_name = "medical";
                        Getcosmatics2(collection_name, String.valueOf(page));
                        return;
                    } else if (checkedId == R.id.chip_Makups) {
                        page = 1;
                        collection_name = "makeup";
                        Getcosmatics2(collection_name, String.valueOf(page));
                        return;
                    } else if (checkedId == R.id.chip_papers) {
                        page = 1;
                        collection_name = "papers";
                        Getcosmatics2(collection_name, String.valueOf(page));
                        return;
                    } else if (checkedId == R.id.chip_others) {
                        page = 1;
                        collection_name = "others";
                        Getcosmatics2(collection_name, String.valueOf(page));
                        return;
                    }
                }

            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isloading = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisibleItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (check && isloading && (visibleItemCount + pastVisibleItems == totalItemCount)) {
                    progressBar.setVisibility(View.VISIBLE);
                    page++;
                    Getcosmatics(collection_name, String.valueOf(page));
                    isloading = false;
                }

            }
        });


    }

    private void Getcosmatics(String collection_name, String page_number) {
        RetrofitClient.getInstance().GETALLPRODUCTSPAGINATION(db.getAllusers().get(0).getToken(), collection_name, page_number).enqueue(new Callback<Retrofit_class_data>() {
            @Override
            public void onResponse(Call<Retrofit_class_data> call, Response<Retrofit_class_data> response) {
                if (response.isSuccessful()) {
                    data2 = response.body().getProducts();
                    adapter.setdate(data2, getActivity(), HomeFragment.this, false);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Retrofit_class_data> call, Throwable t) {
            }
        });

    }

    private void Getcosmatics2(String collection_name, String page_number) {
        RetrofitClient.getInstance().GETALLPRODUCTSPAGINATION(db.getAllusers().get(0).getToken(), collection_name, page_number).enqueue(new Callback<Retrofit_class_data>() {
            @Override
            public void onResponse(Call<Retrofit_class_data> call, Response<Retrofit_class_data> response) {
                if (response.isSuccessful()) {
                    data2.clear();
                    data2 = response.body().getProducts();
                    adapter.setdate2(data2, getActivity(), HomeFragment.this, false);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Retrofit_class_data> call, Throwable t) {
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.search);

        final MenuItem menuItem2 = menu.findItem(R.id.Cart2);
        View actionView = MenuItemCompat.getActionView(menuItem2);
        smsCountTxt = (TextView) actionView.findViewById(R.id.notification_badge);
        setupBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem2);
            }
        });

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
        if (id == R.id.search) {
            return true;
        } else if (id == R.id.Cart2) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, new Order_List()).addToBackStack(null).commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onclick(int position) {

    }

    @Override
    public void onPause() {
        super.onPause();
        //Toast.makeText(getActivity(),"pause",Toast.LENGTH_LONG).show();
        editor.putInt("id", chipGroup.getCheckedChipId());
        editor.putString("collection", collection_name);
        editor.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkid = shp.getInt("id", 0);
        collection_name = shp.getString("collection", "cosmatics");
        if (checkid != 0) {
            chipGroup.check(checkid);
        }
        page = 1;
        Getcosmatics2(collection_name, String.valueOf(page));

        //adapter.notifyDataSetChanged();
        //editor.clear();
    }


    private void setupBadge() {

        if (smsCountTxt != null) {
            if (cart_size == 0) {
                if (smsCountTxt.getVisibility() != View.GONE) {
                    smsCountTxt.setVisibility(View.GONE);
                }
            } else {
                smsCountTxt.setText(String.valueOf(cart_size));
                if (smsCountTxt.getVisibility() != View.VISIBLE) {
                    smsCountTxt.setVisibility(View.VISIBLE);
                }
            }
        }
    }


}