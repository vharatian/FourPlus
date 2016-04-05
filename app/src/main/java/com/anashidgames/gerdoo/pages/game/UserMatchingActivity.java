package com.anashidgames.gerdoo.pages.game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.pages.GerdooActivity;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by psycho on 4/3/16.
 */
public class UserMatchingActivity extends GerdooActivity {

    public static Intent newIntent(Context context){
        return new Intent(context, UserMatchingActivity.class);
    }

    private GifImageView animationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animationView = new GifImageView(this);
        animationView.setImageResource(R.drawable.splash_logo);
        setContentView(animationView);
    }
}

