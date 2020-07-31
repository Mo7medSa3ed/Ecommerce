package com.mohamedsaeed555.ecommerce;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.Product_class;
import com.mohamedsaeed555.MyDataBase.Users;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 0;
    LoginButton loginButton;
    CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient;
    ProgressDialog loadingbar;
    SharedPreferences.Editor editor;
    Users users;
    Button btn;
    TextView txtgo;
    String[] brand_list;
    TextInputLayout emaillayout, passwordlayout;
    AutoCompleteTextView emailtext, passwordtext;
    Database db = new Database(this);
    LottieAlertDialog alertDialog ,alert;
    private String first_name, Name;
    private String last_name;
    private String email;
    private String id;
    private String uri_image;
    private int calc = 0;
    AccessTokenTracker tracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken != null) {
                loaduserprofile(currentAccessToken);
            }


        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        brand_list = getResources().getStringArray(R.array.brand_items);
        emaillayout = findViewById(R.id.inputlayout5);
        passwordlayout = findViewById(R.id.inputlayout1);
        emailtext = findViewById(R.id.filled_exposed_dropdown5);
        passwordtext = findViewById(R.id.filled_exposed_dropdown1);
        txtgo = findViewById(R.id.textView50);
        btn = findViewById(R.id.button7);

        emailtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                emaillayout.setError(null);
            }
        });
        passwordtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passwordlayout.setError(null);
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        loginButton = findViewById(R.id.login_button);
        loadingbar = new ProgressDialog(this);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setColorScheme(SignInButton.COLOR_DARK);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailtext.getText().toString().trim().isEmpty()) {
                    emaillayout.setErrorEnabled(true);
                    emaillayout.setError("Please enter your email");
                    return;
                } else if (passwordtext.getText().toString().trim().isEmpty()) {
                    passwordlayout.setErrorEnabled(true);
                    passwordlayout.setError("Please enter password");
                    return;
                }

                alertDialog = new LottieAlertDialog.Builder(LoginActivity.this, DialogTypes.TYPE_LOADING)
                        .setTitle("Loading")
                        .setDescription("Please wait until Check user")
                        .build();
                alertDialog.setCancelable(false);
                alertDialog.show();

                String name = null;
                String email = emailtext.getText().toString().trim();
                String pass = passwordtext.getText().toString().trim();
                String city = null;
                String address = null;
                String tel = null;
                String img = null;

                btn.setEnabled(false);
                btn.setClickable(false);
                btn.setBackground(getResources().getDrawable(R.drawable.btn_shape_enable));

                users = new Users(name, tel, address, img, email, pass, city, null, null, false, false);
                Add_user(users, true);


            }
        });

        txtgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            if (resultCode == RESULT_OK)
                Add_user(users, false);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null) {
            String name = account.getDisplayName();
            uri_image = account.getPhotoUrl().toString();
            users = new Users(null, null, null, null, null, null, null, null, account.getId(), false, false);
        }
        try {
            GoogleSignInAccount account2 = completedTask.getResult(ApiException.class);
        } catch (ApiException e) {
        }
    }


    private void loaduserprofile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    first_name = object.getString("first_name");
                    last_name = object.getString("last_name");
                    email = object.getString("email");
                    id = object.getString("id");
                    uri_image = "https://graph.facebook.com/" + id + "/picture?type=normal";
                    users = new Users(null, null, null, null, null, null, null, id, null, false, false);
                    Add_user(users, false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle bundle = new Bundle();
        bundle.putString("fields", "first_name,last_name,email,id");
        request.setParameters(bundle);
        request.executeAsync();
    }


    public void Add_user(Users users, Boolean s) {
        RetrofitClient.getInstance().Login_User(users).enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (!(response.isSuccessful())) {

                    try {
                        if (s) {
                            alertDialog.dismiss();
                        }
                        LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(LoginActivity.this, DialogTypes.TYPE_ERROR)
                                .setTitle("Error")
                                .setDescription("Please enter correct password")
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

                        btn.setEnabled(true);
                        btn.setClickable(true);
                        btn.setBackground(getResources().getDrawable(R.drawable.btn_click));

                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                db.Delete_All("Users");
                if (response.body().getToken() != null) {
                    db.insert_user(response.body().getUser(), response.body().getToken());
                } else {
                    db.insert_user(response.body().getUser(), null);
                }

                int size = 0;
                if (response.body().getUser().getFav() != null) {
                    size = response.body().getUser().getFav().size();
                }

                if (size > 0) {
                    for (int x = 0; x < size; x++) {
                        db.insert_fav(response.body().getUser().getFav().get(x));
                    }
                }
                if (s) {
                    alertDialog.dismiss();
                }
                if (response.body().getToken() != null && response.body().getUser().getAdmin()) {
                    alert = new LottieAlertDialog.Builder(LoginActivity.this, DialogTypes.TYPE_LOADING)
                            .setTitle("Loading")
                            .setDescription("Please wait until Get Products Data")
                            .build();
                    alert.setCancelable(false);
                    alert.show();
                    db.Delete_All("AllData");
                    db.Delete_All("BRAND");

                    for (int i = 0; i < brand_list.length; i++) {
                        db.insert_brand(brand_list[i]);
                    }

                    GET(response.body().getToken());



                    if (calc == db.getAllProducts2("AllData").size()) {
                        Intent intent = new Intent(LoginActivity.this, IntroActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(LoginActivity.this, IntroActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    Intent intent = new Intent(LoginActivity.this, IntroActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                if (s) {
                    alertDialog.dismiss();
                }
                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Please enter correct password or Email!")
                        .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                btn.setEnabled(true);
                btn.setClickable(true);
                btn.setBackground(getResources().getDrawable(R.drawable.btn_click));

            }
        });
    }

    public void GET(String token) {
        RetrofitClient.getInstance().GETALLPRODUCTS(token).enqueue(new Callback<List<Product_class>>() {
            @Override
            public void onResponse(Call<List<Product_class>> call, Response<List<Product_class>> response) {
                if (!(response.isSuccessful())) {
                    Toast.makeText(getApplicationContext(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                }
                calc += response.body().size();
                for (Product_class p : response.body()) {
                    db.insert_product_toAlldata("AllData", p);
                }

                try {
                alert.dismiss();
                }catch (Exception e){e.printStackTrace();}

            }

            @Override
            public void onFailure(Call<List<Product_class>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

}
