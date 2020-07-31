package com.mohamedsaeed555.ecommerce;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.Product_class;
import com.mohamedsaeed555.MyDataBase.Users;
import com.mohamedsaeed555.Notification.Notification_Class;
import com.mohamedsaeed555.Notification.Notification_Service;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SecondActivity extends AppCompatActivity {
    final static String FB_URL = "https://www.facebook.com/Makkamedical";
    public static String collection_name = "cosmatics";
    public Boolean check = true;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount acct;
    ArrayList<Product_class> data2 = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    ViewModel viewModel;
    SearchView searchView;
    RecyclerView.LayoutManager layoutManager;
    ChipGroup chipGroup;
    //Database db = new Database(this);
    int page = 1;
    Boolean isloading = true;
    ProgressBar progressBar;
    LinearLayout home, search, logout, tasafoh, cart, updateprofile, Alluser, favourite, orders, changepassword, whats, face, share, location;
    ImageView dropimage;
    TextView addnew, addamount, sale_product, cos, med, mak, pap, oth, username, cartbadge, email;
    ExpandableRelativeLayout myLayout, mylayout2;
    CircleImageView image_uri;
    Gson gson = new Gson();
    Notification_Class notification_class;
    String c = "s";
    Database db = new Database(this);
    int cart_size = 0;
    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;
    private int pastVisibleItems = 0;
    private String check_Activity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Users user = db.getAllusers().get(0);
       // Toast.makeText(this,String.valueOf(db.getAllProducts2("AllData").size()),Toast.LENGTH_LONG).show();
        StartService2();

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        cart_size = db.getAllProducts("Cart").size();


        try {
            c = getIntent().getExtras().getString("c", "f");
            notification_class = gson.fromJson(getIntent().getExtras().getString("go", ""), Notification_Class.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (c.equals("c")) {
            if (notification_class.getGo_Activity().equals("details")) {
                Bundle bundle = new Bundle();
                bundle.putString("col", notification_class.getCollection());
                bundle.putString("pd", gson.toJson(notification_class.getProduct_class()));
                DetailsProductActivity frag = new DetailsProductActivity();
                frag.setArguments(bundle);
                fragmentTransaction.replace(R.id.cotainers, frag)
                        .addToBackStack(null).commit();
            } else if (notification_class.getGo_Activity().equals("orderdetails")) {
                Bundle bundle = new Bundle();
                bundle.putString("os", gson.toJson(notification_class.getOrd()));
                Orders_Details frag = new Orders_Details();
                frag.setArguments(bundle);
                fragmentTransaction.replace(R.id.cotainers, frag)
                        .addToBackStack(null).commit();
            } else if (notification_class.getGo_Activity().equals("alluser")) {
                fragmentTransaction.replace(R.id.cotainers,new  AlluserFragment())
                        .addToBackStack(null).commit();
            }
        } else {
            fragmentTransaction.replace(R.id.cotainers, new HomeFragment()).commit();
        }


        NavigationView navigationView = findViewById(R.id.navigation);
        home = (LinearLayout) navigationView.findViewById(R.id.linear1);
        search = (LinearLayout) navigationView.findViewById(R.id.linear3);
        tasafoh = (LinearLayout) navigationView.findViewById(R.id.linear4);
        logout = (LinearLayout) navigationView.findViewById(R.id.linear5);
        cart = (LinearLayout) navigationView.findViewById(R.id.linear8);
        username = navigationView.findViewById(R.id.user_name);
        email = navigationView.findViewById(R.id.email);
        image_uri = navigationView.findViewById(R.id.profile_image);
        updateprofile = navigationView.findViewById(R.id.linear6);
        Alluser = navigationView.findViewById(R.id.linearuser);
        favourite = navigationView.findViewById(R.id.linearfavourite);
        if (user.get_id() != null) {
            if (user.getName() != null){
                username.setText(user.getName());
                email.setText(user.getEmail());}
            if (user.getImage() != null)
                Picasso.get().load(Uri.parse(user.getImage())).placeholder(R.drawable.makkah).into(image_uri);
        }

        LinearLayout btn_addactivity = navigationView.findViewById(R.id.btn);
        final ImageView arrow = navigationView.findViewById(R.id.arrow);
        final ImageView arrow2 = navigationView.findViewById(R.id.arrow2);
        myLayout = navigationView.findViewById(R.id.expandableLayout1);
        mylayout2 = navigationView.findViewById(R.id.expandableLayout2);
        cos = navigationView.findViewById(R.id.tv_chiled3);
        med = navigationView.findViewById(R.id.tv_chiled4);
        mak = navigationView.findViewById(R.id.tv_chiled5);
        pap = navigationView.findViewById(R.id.tv_chiled6);
        oth = navigationView.findViewById(R.id.tv_chiled7);
        addnew = navigationView.findViewById(R.id.tv_chiled);
        addamount = navigationView.findViewById(R.id.tv_chiled2);
        sale_product = navigationView.findViewById(R.id.tv_sale);
        orders = navigationView.findViewById(R.id.linearorders);
        changepassword = navigationView.findViewById(R.id.linearpassword);
        cartbadge = navigationView.findViewById(R.id.notification_badge);
        whats = navigationView.findViewById(R.id.linearwhats);
        face = navigationView.findViewById(R.id.linearface);
        share = navigationView.findViewById(R.id.linearshare);
        location = navigationView.findViewById(R.id.linearlocation);

        setupBadge(cart_size);

        if (user.getToken() == null || user.getAdmin() == false) {
            btn_addactivity.setVisibility(View.GONE);
            Alluser.setVisibility(View.GONE);
            orders.setVisibility(View.GONE);
            tasafoh.setVisibility(View.GONE);
            search.setVisibility(View.GONE);

        }

        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", "");
                AddNewProduct product = new AddNewProduct();
                product.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, product).addToBackStack(null).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        image_uri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, new UserSetting()).addToBackStack(null).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        addamount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("collection_name", "");
                bundle.putString("barcode", "");
                bundle.putString("name", "");
                bundle.putString("date", "");
                bundle.putDouble("price", 0);
                bundle.putString("brand", "");
                bundle.putString("image", "");
                bundle.putInt("amount", 0);
                AmountFragment myFragment = new AmountFragment();
                myFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, myFragment).addToBackStack(null).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }

        });
///////////////////////////////

        sale_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("collection_name", "");
                bundle.putString("barcode", "");
                bundle.putString("name", "");
                bundle.putString("date", "");
                bundle.putDouble("price", 0);
                bundle.putString("brand", "");
                bundle.putString("image", "");
                bundle.putInt("amount", 0);
                SaleFragment myFragment = new SaleFragment();
                myFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, myFragment).addToBackStack(null).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }

        });


        btn_addactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //myLayout.toggle();

                if (myLayout.isExpanded()) {
                    myLayout.collapse();
                    arrow.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);

                } else {
                    myLayout.expand();
                    arrow.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);


                }
            }
        });


        tasafoh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mylayout2.isExpanded()) {
                    mylayout2.collapse();
                    arrow2.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);

                } else {
                    mylayout2.expand();
                    arrow2.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                }


            }


        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, new Order_List()).addToBackStack(null).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });


        cos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GO_Activity("Cosmatics");
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GO_Activity("Medical");
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        mak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GO_Activity("Makeup");
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        pap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GO_Activity("Papers");
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        oth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GO_Activity("Others");
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });


        updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, new UserSetting()).addToBackStack(null).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });


        Alluser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, new AlluserFragment()).addToBackStack(null).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, new FavouriteFragment()).addToBackStack(null).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, new ChangePassword()).addToBackStack(null).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        acct = GoogleSignIn.getLastSignedInAccount(this);

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, new OrdersFragment()).addToBackStack(null).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, new ScannerViewForSearch()).addToBackStack(null).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationInfo info = getApplicationContext().getApplicationInfo();
                String apkpath = info.sourceDir;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("application/vnd.android.package-archive");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(apkpath)));
                startActivity(Intent.createChooser(intent, "SHARE APP USING"));
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent2 = new Intent(Intent.ACTION_VIEW);
                    intent2.setData(Uri.parse("https://www.google.com/maps/place/%D9%85%D9%83%D9%87+%D9%84%D9%84%D9%85%D8%B3%D8%AA%D9%84%D8%B2%D9%85%D8%A7%D8%AA+%D8%A7%D9%84%D8%B7%D8%A8%D9%8A%D8%A9+%D9%88+%D9%85%D8%B3%D8%AA%D8%AD%D8%B6%D8%B1%D8%A7%D8%AA+%D8%A7%D9%84%D8%AA%D8%AC%D9%85%D9%8A%D9%84%E2%80%AD/@30.517151,31.353748,19.25z/data=!4m16!1m10!4m9!1m4!2m2!1d31.3427256!2d30.5135962!4e1!1m3!2m2!1d31.3531244!2d30.5170797!3m4!1s0x14f7e79878b898a3:0xe6953de154273ffd!8m2!3d30.5170772!4d31.3532511"));
                    startActivity(Intent.createChooser(intent2, "Launch Map"));

            }
        });

        whats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appinstalledornot("com.whatsapp")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "0201000995944"));
                    startActivity(intent);
                } else {
                    Toast.makeText(SecondActivity.this, "WhatsApp doesn't installed on your device", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
            }

        });

        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotofacebook();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null) {
                    db.Delete_All("Users");
                    db.Delete_All("Cart");
                    db.Delete_All("Products");
                    db.Delete_All("Favourite");

                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(SecondActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else if (acct != null) {
                    db.Delete_All("Users");
                    db.Delete_All("Cart");
                    db.Delete_All("Products");
                    db.Delete_All("Favourite");

                    signOut();
                } else {
                    db.Delete_All("Users");
                    db.Delete_All("Cart");
                    db.Delete_All("Products");
                    db.Delete_All("Favourite");
                    Intent intent = new Intent(SecondActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

      /*  addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, ScannerView.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });*/


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, new HomeFragment()).addToBackStack(null).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });


    }


    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
            //adapter.notifyDataSetChanged();
        }
    }


    public void GO_Activity(String key) {
        Bundle bundle = new Bundle();
        bundle.putString("activity", key);
        Fragment fragment = new AdminFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, fragment).addToBackStack(null).commit();
    }

    private void setupBadge(int count) {

        if (cartbadge != null) {
            if (count == 0) {
                if (cartbadge.getVisibility() != View.GONE) {
                    cartbadge.setVisibility(View.GONE);
                }
            } else {
                cartbadge.setText(String.valueOf(count));
                if (cartbadge.getVisibility() != View.VISIBLE) {
                    cartbadge.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    public void StartService() {
        Intent intent = new Intent(this, Notification_Service.class);
        ContextCompat.startForegroundService(this, intent);
    }


    public void StopService() {
        Intent intent = new Intent(this, Notification_Service.class);
        stopService(intent);
    }

    public void StartService2() {
        Intent intent = new Intent(this, Notification_Service.class);
        startService(intent);
    }

    public boolean appinstalledornot(String url) {
        PackageManager packageManager = getPackageManager();
        boolean installed;
        try {
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    public void gotofacebook() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("fb://facewebmodal/f?href=" + FB_URL));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(FB_URL));
            startActivity(intent);
        }
    }
}
