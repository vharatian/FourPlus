package com.anashidgames.gerdoo.pages;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.anashidgames.gerdoo.R;

/**
 * Created by psycho on 3/29/16.
 */
public class FragmentContainerActivity extends GerdooActivity {

    private int fragmentId;

    public FragmentContainerActivity(int fragmentId) {
        this.fragmentId = fragmentId;
    }

    public void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.fade_out);
        transaction.replace(fragmentId, fragment);

        transaction.addToBackStack(null);
        transaction.commit();

    }

    public Fragment getInnerFragment(){
        return getSupportFragmentManager().findFragmentById(fragmentId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getInnerFragment();
        if (fragment != null){
            fragment.onActivityResult(requestCode, resultCode, data);
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if(backStackEntryCount <= 1){
            finish();
        }
        else{
            super.onBackPressed();
        }
    }
}
