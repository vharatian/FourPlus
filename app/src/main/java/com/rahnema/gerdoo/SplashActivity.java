package com.rahnema.gerdoo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rahnema.gerdoo.intro.IntroductionActivity;

import java.util.Date;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    private Date startDate;
    private GifImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startDate = new Date();

        imageView = (GifImageView) findViewById(R.id.imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        imageView.setImageResource(R.drawable.splash_logo);
        GifDrawable drawable = (GifDrawable) imageView.getDrawable();
        drawable.addAnimationListener(new InnerAnimationListener());
    }

    private class InnerAnimationListener implements AnimationListener {
        @Override
        public void onAnimationCompleted() {
            imageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    startActivity(IntroductionActivity.newIntent(SplashActivity.this));
                }
            }, 1000);

        }
    }
}
