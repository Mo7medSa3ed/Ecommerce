package com.mohamedsaeed555.ecommerce;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.Users;

import org.jetbrains.annotations.NotNull;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangePassword extends Fragment {

    TextInputLayout oldpass, new_pass, confirm;
    AutoCompleteTextView oldtext, new_text, Confirm_Text;
    Button change;
    Users users;
    Database db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new Database(getActivity());
        users = db.getAllusers().get(0);
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        oldpass = view.findViewById(R.id.inputlayout1);
        new_pass = view.findViewById(R.id.inputlayout2);
        confirm = view.findViewById(R.id.inputlayout3);
        oldtext = view.findViewById(R.id.filled_exposed_dropdown1);
        new_text = view.findViewById(R.id.filled_exposed_dropdown2);
        Confirm_Text = view.findViewById(R.id.filled_exposed_dropdown3);
        change = view.findViewById(R.id.button4);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (oldtext.getText().toString().trim().isEmpty()) {
                    oldpass.setErrorEnabled(true);
                    oldpass.setError("Please enter old password ,first");
                } else if (new_text.getText().toString().trim().isEmpty()) {
                    new_pass.setErrorEnabled(true);
                    new_pass.setError("Please enter new password");
                } else if (Confirm_Text.getText().toString().trim().isEmpty()) {
                    confirm.setErrorEnabled(true);
                    confirm.setError("Please rewrite new password again");
                }

                String new_pass, confirmpass;
                new_pass = new_text.getText().toString().trim();
                confirmpass = Confirm_Text.getText().toString().trim();
                if (new_pass.equals(confirmpass)) {

                    RetrofitClient.getInstance().ChangePassword(users.getToken(), users.get_id(), confirmpass).enqueue(new Callback<Users>() {
                        @Override
                        public void onResponse(Call<Users> call, Response<Users> response) {
                            if (response.isSuccessful()) {
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Change Password")
                                        .setContentText("Password Changed successfully")
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
                    });

                } else {
                    Confirm_Text.setText("");
                    confirm.setErrorEnabled(true);
                    confirm.setError("Re write password again ,please");
                    LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(getActivity(), DialogTypes.TYPE_ERROR)
                            .setTitle("Error")
                            .setDescription("Make sure confirm password please")
                            .setPositiveText("Okay")
                            .setPositiveListener(new ClickListener() {
                                @Override
                                public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                                    lottieAlertDialog.dismiss();
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.cotainers, new HomeFragment()).commit();
                                }
                            })
                            .build();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }


            }
        });

    }
}
