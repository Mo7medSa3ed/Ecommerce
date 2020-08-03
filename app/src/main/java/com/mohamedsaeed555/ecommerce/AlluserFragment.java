package com.mohamedsaeed555.ecommerce;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.Users;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AlluserFragment extends Fragment {

    ListView list;
    UserAdapter adapter;
    ArrayList<Users> users = new ArrayList<>();
    Database db;
    Users user;
    SwipeRefreshLayout refreshLayout;
    String i ="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            i = getArguments().getString("i", "f");
        }catch (Exception e){e.printStackTrace();}
        db = new Database(getActivity());
        user = db.getAllusers().get(0);
        return inflater.inflate(R.layout.fragment_alluser, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle("All Users");
        list = view.findViewById(R.id.userlist);
        refreshLayout=view.findViewById(R.id.Swipefresh);

        // users.add(new Users("dsdsa","asd","sadasd","adsd","asdsad","asdsa","sda","asdas","ads",false,false,"sadsa"));

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllUser();
                refreshLayout.setRefreshing(false);
            }
        });

        adapter = new UserAdapter(users, getActivity());
        list.setAdapter(adapter);
        getAllUser();
       // if (i.equals("u")) {}
            //list.setSelection(users.size()-1);

        list.smoothScrollToPosition(users.size()-1);
    }


    public void getAllUser() {
        RetrofitClient.getInstance().GET_all_User(user.getToken()).enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                if (response.isSuccessful()) {
                    users.clear();
                    for (Users u : response.body()) {
                        if (!(u.get_id().equals(user.get_id())))
                            users.add(u);
                    }
                    //Toast.makeText(getActivity(),String.valueOf(users.size()),Toast.LENGTH_LONG).show();
                    adapter.setdata(users);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.deleteall, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Database db = new Database(getActivity());
        int id2 = item.getItemId();
        if (id2 == R.id.deleteall) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("You want to delete all users!")
                    .setConfirmText("Yes,delete it!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            RetrofitClient.getInstance().DeleteAllUser(db.getAllusers().get(0).getToken()).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        sweetAlertDialog.dismissWithAnimation();
                                        users.clear();
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
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
                                    LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(getActivity(), DialogTypes.TYPE_ERROR)
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
        }
        return super.onOptionsItemSelected(item);
    }

}
