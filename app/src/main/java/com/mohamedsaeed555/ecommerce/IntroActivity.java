package com.mohamedsaeed555.ecommerce;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addSlide(AppIntroFragment.newInstance("Cosmatics",
                "",
                R.drawable.cosm,
                Color.parseColor("#FFE4B5")));


        addSlide(AppIntroFragment.newInstance("Medical Supplies",
                "",
                R.drawable.med,
                Color.parseColor("#5dc425")));

        addSlide(AppIntroFragment.newInstance("Baby Products",
                "",
                R.drawable.baby,
                Color.parseColor("#f0add0")));


        showStatusBar(true);

       // setBarColor(Color.parseColor("#0B0E4D"));
       // setSeparatorColor(Color.parseColor("#0B0E4D"));

        setFadeAnimation();
        //setZoomAnimation();
        // setFlowAnimation();
        //  setCustomTransformer(new ZoomOutPageTransformer());

    }

    @Override
    public void onDonePressed() {
        startActivity(new Intent(this, SecondActivity.class));
        finish();
    }

    @Override
    public void onSkipPressed() {
        startActivity(new Intent(this, SecondActivity.class));
        finish();
    }

}
