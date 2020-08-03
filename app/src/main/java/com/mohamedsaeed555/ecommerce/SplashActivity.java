package com.mohamedsaeed555.ecommerce;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.Product_class;


import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;


public class SplashActivity extends AppCompatActivity {

    Database db = new Database(this);
    GifImageView gifImageView ;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        linearLayout = findViewById(R.id.anim_layout);
        gifImageView=findViewById(R.id.gifimage);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.down);
        linearLayout.startAnimation(animation);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3300);
                        if (db.getAllusers().size() != 0 || db.getAllusers().size() > 0) {
                            //second
                            Intent intent = new Intent(SplashActivity.this, SecondActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            //main
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }else {
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }

    }







}
