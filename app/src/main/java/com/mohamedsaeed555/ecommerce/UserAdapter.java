package com.mohamedsaeed555.ecommerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.Users;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends BaseAdapter {

    ArrayList<Users> arrayList = new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;
    RadioGroup group;

    public UserAdapter() {
    }

    public UserAdapter(ArrayList<Users> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public void setdata(ArrayList<Users> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.user_item, null, false);
        }

        CircleImageView img = view.findViewById(R.id.user_image);
        TextView txtname = view.findViewById(R.id.textView38);
        TextView txtemail = view.findViewById(R.id.textView40);
        ImageButton btn_delete = view.findViewById(R.id.button6);
        group = view.findViewById(R.id.group);

        Picasso.get().load(arrayList.get(position).getImage()).placeholder(R.drawable.makkah).into(img);
        txtname.setText(arrayList.get(position).getName());
        txtemail.setText(arrayList.get(position).getEmail());
        if (arrayList.get(position).getAdmin()) {
            group.check(R.id.admin);
        } else {
            group.check(R.id.user);
        }
        final  int pos = position;
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database db = new Database(context);
                String _id = arrayList.get(pos).get_id();
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You want to delete this user!")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                RetrofitClient.getInstance().DeleteUser(db.getAllusers().get(0).getToken(), _id).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()) {

                                            arrayList.remove(pos);
                                            notifyDataSetChanged();
                                            sweetAlertDialog.dismissWithAnimation();

                                            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("Delete Product")
                                                    .setContentText("product deleted successfully")
                                                    .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            sweetAlertDialog.dismissWithAnimation();
                                                        }
                                                    })
                                                    .show();

                                        } else {
                                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
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
                                        LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(context, DialogTypes.TYPE_ERROR)
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

            }
        });

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.user) {
                    make_user(arrayList.get(position).getToken(), arrayList.get(position).get_id(), false);
                } else if (checkedId == R.id.admin) {
                    make_admin(arrayList.get(position).getToken(), arrayList.get(position).get_id(), true);
                }
            }
        });


        return view;
    }

    public void make_user(String token, String id, Boolean admin) {
        RetrofitClient.getInstance().UpdateUserAdmin(token, id, admin).enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Update User")
                            .setContentText("This User Become Now User")
                            .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    group.check(R.id.user);
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
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
            public void onFailure(Call<Users> call, Throwable t) {
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
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
        });
    }

    public void make_admin(String token, String id, Boolean admin) {
        RetrofitClient.getInstance().UpdateUserAdmin(token, id, admin).enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Update User")
                            .setContentText("This User Become Now Admin")
                            .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    group.check(R.id.admin);
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
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
            public void onFailure(Call<Users> call, Throwable t) {
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
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
        });
    }
}
