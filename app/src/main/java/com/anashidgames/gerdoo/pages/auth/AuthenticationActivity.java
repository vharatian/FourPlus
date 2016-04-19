package com.anashidgames.gerdoo.pages.auth;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.anashidgames.gerdoo.core.DataHelper;
import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.pages.FragmentContainerActivity;
import com.anashidgames.gerdoo.pages.GerdooActivity;
import com.anashidgames.gerdoo.pages.home.HomeActivity;

public class AuthenticationActivity extends FragmentContainerActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, AuthenticationActivity.class);
    }


    public AuthenticationActivity() {
        super(R.id.fragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        changeFragment(SignUpFragment.newInstance());
    }

    public void enter() {
        finish();
        startActivity(HomeActivity.newIntent(this));
    }
}
