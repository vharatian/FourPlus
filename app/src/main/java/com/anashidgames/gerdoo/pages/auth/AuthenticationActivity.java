package com.anashidgames.gerdoo.pages.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.GerdooApplication;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.pages.FragmentContainerActivity;
import com.anashidgames.gerdoo.pages.home.HomeActivity;

public class AuthenticationActivity extends FragmentContainerActivity {

    public static final String START_HOME = "startHome";
    private boolean authenticated;


    public static Intent newIntent(Context context, boolean startHome) {
        Intent intent = new Intent(context, AuthenticationActivity.class);
        intent.putExtra(START_HOME, startHome);
        return intent;
    }

    private boolean startHome;

    public AuthenticationActivity() {
        super(R.id.fragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_fragment);

        startHome = getIntent().getBooleanExtra(START_HOME, true);

        changeFragment(SignUpFragment.newInstance());
    }

    @Override
    protected void onResume() {
        super.onResume();
        GerdooServer.INSTANCE.getAuthenticationManager().authenticationPageIsOnScreen(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        GerdooServer.INSTANCE.getAuthenticationManager().authenticationPageIsNotOnScreen(this);
    }

    public void notifyAuthenticated() {
        finish();

        authenticated = true;
        if (startHome) {
            startActivity(HomeActivity.newIntent(this));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!authenticated){
            GerdooApplication.closeApplication();
        }
    }
}
