package com.anashidgames.gerdoo.pages.home;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.pages.FragmentContainerActivity;
import com.anashidgames.gerdoo.pages.GerdooActivity;

public class HomeActivity extends FragmentContainerActivity {

    public HomeActivity() {
        super(R.id.fragment);
    }

    public static Intent newIntent(Context context){
        return new Intent(context, HomeActivity.class);
    }

    private View logoView;
    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initToolbar();

        changeFragment(HomeFragment.newInstance());
    }


    public void setTitle(String title){
        if (title == null){
            titleView.setVisibility(View.GONE);
            logoView.setVisibility(View.VISIBLE);
        }else {
            logoView.setVisibility(View.GONE);
            titleView.setVisibility(View.VISIBLE);

            titleView.setText(title);
        }
    }



    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        logoView = findViewById(R.id.logoView);
        titleView = (TextView) findViewById(R.id.titleView);
    }
}
