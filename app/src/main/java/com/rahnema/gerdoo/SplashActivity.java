package com.rahnema.gerdoo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rahnema.gerdoo.auth.AuthenticationActivity;
import com.rahnema.gerdoo.core.DataHelper;
import com.rahnema.gerdoo.core.service.GerdooServer;
import com.rahnema.gerdoo.core.service.callback.CallbackWithErrorDialog;
import com.rahnema.gerdoo.home.HomeActivity;
import com.rahnema.gerdoo.intro.IntroductionActivity;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

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
        public void onAnimationCompleted() {
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
        public void handleSuccessful(Response<Boolean> response) {
            authenticated = response.body();
            checkState();
        }

        @Override
        protected void postError() {
            super.postError();
            finish();
        }
    }
}
