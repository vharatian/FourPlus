package com.anashidgames.gerdoo.pages.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.pages.FragmentContainerActivity;
import com.anashidgames.gerdoo.pages.GerdooActivity;

/**
 * Created by psycho on 6/5/16.
 */
public class CompleteRegistrationActivity extends FragmentContainerActivity {

    public static Intent newIntent(Context context){
        return new Intent(context, CompleteRegistrationActivity.class);
    }


    public CompleteRegistrationActivity() {
        super(R.id.fragment);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_fragment);

        changeFragment(CompleteRegistrationFragment.newInstance());
    }
}
