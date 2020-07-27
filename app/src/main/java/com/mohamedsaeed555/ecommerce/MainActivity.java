package com.mohamedsaeed555.ecommerce;

import android.content.Intent;
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
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.Product_class;
import com.mohamedsaeed555.MyDataBase.Users;
import com.mohamedsaeed555.Notification.Notification_Class;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 0;
    String[] brand_list;
    LoginButton loginButton;
    CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient;
    int calc = 0;
    Users users;
    ExpandableRelativeLayout layout;
    TextView txtgo;
    TextInputLayout namelayout, citylayout, emaillayout, passwordlayout, mobilelayout, addresslayout;
    AutoCompleteTextView nametext, citytext, emailtext, passwordtext, mobiletext, addresstext;
    Database db = new Database(this);
    Button btn;
    boolean check = true;
    LottieAlertDialog alertDialog;
    Gson gson = new Gson();
    private String first_name, Name;
    private String last_name;
    private String email;
    private String id;
    private String uri_image;
    private Socket mSocket;
    AccessTokenTracker tracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken != null) {
                loaduserprofile(currentAccessToken);
            }
        }
    };

    {
        try {
            mSocket = IO.socket("https://newaccsys.herokuapp.com");
        } catch (URISyntaxException e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        // final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        layout = findViewById(R.id.expandle);
        //layout.startAnimation(myAnim);
        txtgo = findViewById(R.id.textView39);
        btn = findViewById(R.id.button4);
        namelayout = findViewById(R.id.inputlayout2);
        citylayout = findViewById(R.id.inputlayout4);
        emaillayout = findViewById(R.id.inputlayout5);
        passwordlayout = findViewById(R.id.inputlayout1);
        mobilelayout = findViewById(R.id.inputlayout3);
        addresslayout = findViewById(R.id.inputlayout);
        nametext = findViewById(R.id.filled_exposed_dropdown2);
        citytext = findViewById(R.id.filled_exposed_dropdown4);
        emailtext = findViewById(R.id.filled_exposed_dropdown5);
        passwordtext = findViewById(R.id.filled_exposed_dropdown1);
        mobiletext = findViewById(R.id.filled_exposed_dropdown3);
        addresstext = findViewById(R.id.filled_exposed_dropdown);
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

        brand_list = getResources().getStringArray(R.array.brand_items);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        loginButton = findViewById(R.id.login_button);

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

                btn.setEnabled(true);
                btn.setClickable(true);


                if (nametext.getText().toString().trim().isEmpty()) {
                    namelayout.setErrorEnabled(true);
                    namelayout.setError("Please enter your name");
                    return;
                } else if (emailtext.getText().toString().trim().isEmpty()) {
                    emaillayout.setErrorEnabled(true);
                    emaillayout.setError("Please enter your email");
                    return;
                } else if (passwordtext.getText().toString().trim().isEmpty()) {
                    passwordlayout.setErrorEnabled(true);
                    passwordlayout.setError("Please enter password");
                    return;
                }

                alertDialog = new LottieAlertDialog.Builder(MainActivity.this, DialogTypes.TYPE_LOADING)
                        .setTitle("Loading")
                        .setDescription("Please wait until User Created")
                        .build();
                alertDialog.setCancelable(false);
                alertDialog.show();

                String name = nametext.getText().toString().trim();
                String email = emailtext.getText().toString().trim();
                String pass = passwordtext.getText().toString().trim();
                String city = null;
                String address = null;
                String tel = null;
                String img = null;

                if (!(addresstext.getText().toString().trim().equals(""))) {
                    address = addresstext.getText().toString().trim();
                }

                if (!(citytext.getText().toString().trim().equals(""))) {

                    city = citytext.getText().toString().trim();
                }

                if (!(mobiletext.getText().toString().trim().equals(""))) {
                    tel = mobiletext.getText().toString().trim();
                }

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
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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

            users = new Users(name, null, null, uri_image, account.getEmail(), null, null, null, account.getId(), false, false);

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
                    users = new Users(first_name + " " + last_name, null, null, uri_image, email, null, null, id, null, false, false);
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
        RetrofitClient.getInstance().Add_User(users).enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (!(response.isSuccessful())) {
                    if (s) {
                        alertDialog.dismiss();
                    }
                    LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(MainActivity.this, DialogTypes.TYPE_ERROR)
                            .setTitle("Error")
                            .setDescription("this user existed before ,please add new user.")
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
                   /* new SweetAlertDialog(MainActivity.this(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!")
                            .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();*/

                    Toast.makeText(getApplicationContext(), "This User Existed Before ,please add new user.", Toast.LENGTH_LONG).show();

                    btn.setEnabled(true);
                    btn.setClickable(true);
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_click));


                    return;
                }
                Notification_Class notification_class = new Notification_Class(response.body().getAdmin(), "New User Create Account", "alluser");
                mSocket.emit("dbchanged", gson.toJson(notification_class));

                db.Delete_All("Users");
                db.insert_user(response.body(), null);

                int size = response.body().getFav().size();
                if (size > 0) {
                    for (int x = 0; x < size; x++) {
                        db.insert_fav(response.body().getFav().get(x));
                    }
                }
                if (s) {
                    alertDialog.dismiss();
                }

                if (response.body().getAdmin() && response.body().getToken() != null) {
                    alertDialog = new LottieAlertDialog.Builder(MainActivity.this, DialogTypes.TYPE_LOADING)
                            .setTitle("Loading")
                            .setDescription("Please wait until Get Products Data")
                            .build();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                    db.Delete_All("AllData");
                    db.Delete_All("BRAND");

                    for (int i = 0; i < brand_list.length; i++) {
                        db.insert_brand(brand_list[i]);
                    }

                    GET(response.body().getToken(), "cosmatics");
                    GET(response.body().getToken(), "medical");
                    GET(response.body().getToken(), "papers");
                    GET(response.body().getToken(), "makeup");
                    GET(response.body().getToken(), "others");

                    if (calc == db.getAllProducts2("AllDate").size()) {
                        Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                    startActivity(intent);
                    finish();
                }


            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                if (s) {
                    alertDialog.dismiss();
                }
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Something went wrong!")
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

    public void GET(String token, String collection_name) {
        RetrofitClient.getInstance().GETALLPRODUCTS(token, collection_name).enqueue(new Callback<List<Product_class>>() {
            @Override
            public void onResponse(Call<List<Product_class>> call, Response<List<Product_class>> response) {
                if (!(response.isSuccessful())) {
                    check = false;
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!,please try again later.")
                            .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                    return;
                }
                calc += response.body().size();
                for (Product_class p : response.body()) {
                    Product_class productClass = new Product_class(p.getDate(), p.getAmount(),
                            p.getBarcode(), p.getName(), p.getPrice(), p.getBrand(),
                            p.getImage(), collection_name);
                    db.insert_product_toAlldata("AllData", productClass);
                }
                if (collection_name.equals("others")) {
                    alertDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<List<Product_class>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                check = false;
                return;
            }
        });
    }

}
