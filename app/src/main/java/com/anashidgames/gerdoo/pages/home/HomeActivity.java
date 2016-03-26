package com.anashidgames.gerdoo.pages.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.anashidgames.gerdoo.R;

public class HomeActivity extends AppCompatActivity {

    public static Intent newIntent(Context context){
        return new Intent(context, HomeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}
