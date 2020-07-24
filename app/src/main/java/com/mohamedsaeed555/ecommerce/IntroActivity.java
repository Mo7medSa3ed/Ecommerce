package com.mohamedsaeed555.ecommerce;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addSlide(AppIntroFragment.newInstance("Mohamed",
                "Saeed",
                R.drawable.ic_launcher_background,
                Color.parseColor("#51e2b7")));


        addSlide(AppIntroFragment.newInstance("Mohamed",
                "HAbiby",
                R.drawable.ic_launcher_background,
                Color.parseColor("#0B0E4D")));

        addSlide(AppIntroFragment.newInstance("Mohamed",
                "Elawah",
                R.drawable.ic_launcher_background,
                Color.parseColor("#51e2b7")));


        showStatusBar(true);
        setBarColor(Color.parseColor("#0B0E4D"));
        setSeparatorColor(Color.parseColor("#0B0E4D"));

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

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
