package com.anashidgames.gerdoo.pages.auth;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.anashidgames.gerdoo.core.DataHelper;
import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.pages.home.HomeActivity;

public class AuthenticationActivity extends AppCompatActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, AuthenticationActivity.class);
    }

    private DataHelper dataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        dataHelper = new DataHelper(this);

        changeFragment(SignUpFragment.newInstance());
    }

    public void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.fragment, fragment);

        transaction.addToBackStack(null);
        transaction.commit();

    }

    public Fragment getInnerFragment(){
        return getSupportFragmentManager().findFragmentById(R.id.fragment);
    }

    @Override
    public void onBackPressed() {
        if (getInnerFragment() instanceof SignUpFragment)
            finish();
        else
            super.onBackPressed();


    }

    public void enter(String sessionKey) {
        if (sessionKey == null)
            return;

        dataHelper.setSessionKey(sessionKey);
        finish();
        startActivity(HomeActivity.newIntent(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getInnerFragment();
        if (fragment != null){
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
