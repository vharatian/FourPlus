package com.anashidgames.gerdoo.pages.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.pages.FragmentContainerActivity;

/**
 * Created by psycho on 6/12/16.
 */
public class EditProfileActivity extends FragmentContainerActivity {
    public static Intent newIntent(Context context) {
        return new Intent(context, EditProfileActivity.class);
    }


    public EditProfileActivity() {
        super(R.id.fragment);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_fragment);

        changeFragment(EditProfileFragment.newInstance());
    }
}
