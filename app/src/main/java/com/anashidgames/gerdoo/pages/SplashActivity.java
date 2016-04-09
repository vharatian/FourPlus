package com.anashidgames.gerdoo.pages;

import android.content.Intent;
import android.os.Bundle;

import com.anashidgames.gerdoo.core.DataHelper;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.callback.CallbackWithErrorDialog;
import com.anashidgames.gerdoo.pages.auth.AuthenticationActivity;
import com.anashidgames.gerdoo.pages.intro.IntroductionActivity;
import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.pages.home.HomeActivity;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;

public class SplashActivity extends GerdooActivity {

    private GifImageView imageView;

    private boolean animationFinished = false;
    private Boolean authenticated = null;
    private Boolean firstTime = null;

    private DataHelper dataHelper;
    private GerdooServer server;
    private Call<Boolean> sessionCheckCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        dataHelper = new DataHelper(this);
        server = new GerdooServer();

        imageView = (GifImageView) findViewById(R.id.imageView);
        new Thread(new FirstTimeChecker()).start();
        new Thread(new AuthenticationChecker()).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        imageView.setImageResource(R.drawable.splash_logo);
        GifDrawable drawable = (GifDrawable) imageView.getDrawable();
        drawable.addAnimationListener(new InnerAnimationListener());
    }

    @Override
    protected void onStop() {
        super.onStop();

        dataHelper.setFirstTime();
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public void setAnimationFinished(boolean animationFinished) {
        this.animationFinished = animationFinished;
    }



    private synchronized void checkState() {
        if (animationFinished) {

            Intent intent;
            if(firstTime == null)
                intent = null;
            else if(firstTime)
                intent = IntroductionActivity.newIntent(this);
            else if(authenticated == null)
                intent = null;
            else if(!authenticated)
                intent = AuthenticationActivity.newIntent(this);
            else
                intent = HomeActivity.newIntent(this);


            if (intent != null){
                finish();
                startActivity(intent);

                if(sessionCheckCall != null)
                    sessionCheckCall.cancel();
            }
        }
    }

    private class InnerAnimationListener implements AnimationListener {
        @Override
        public void onAnimationCompleted(int loopNumber) {
            imageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setAnimationFinished(true);
                    checkState();
                }
            }, 1000);
        }
    }

    private class FirstTimeChecker implements Runnable {
        @Override
        public void run() {
            setFirstTime(dataHelper.isFirstTime());
            checkState();
        }
    }

    private class AuthenticationChecker implements Runnable {
        @Override
        public void run() {
            String sessionKey = dataHelper.getSessionKey();
            if (sessionKey == null){
                setAuthenticated(false);
                checkState();
            }else{
                sessionCheckCall = server.checkSession(sessionKey);
                sessionCheckCall.enqueue(new SessionKeyCheckCallback());
            }
        }
    }

    private class SessionKeyCheckCallback extends CallbackWithErrorDialog<Boolean> {

        public SessionKeyCheckCallback() {
            super(SplashActivity.this);
        }

        @Override
        public void handleSuccessful(Boolean data) {
            authenticated = data;
            checkState();
        }

        @Override
        protected void postError() {
            super.postError();
            finish();
        }

    }
}
