package com.rahnema.gerdoo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Date;

import pl.droidsonroids.gif.GifImageView;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    private Date startDate;
    private GifImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startDate = new Date();

        imageView = (GifImageView) findViewById(R.id.imageView);
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(R.drawable.splash_logo);
            }
        }, 500);

//        final MatchButton button = (MatchButton) findViewById(R.id.button);
//        button.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                button.setStatus(MatchButton.ButtonStatus.Success);
//                button.setUserSelected(true);
//                button.setOpponentSelected(true);
//            }
//        }, 2000);

//        startActivity(new Intent(this, AccountActivity.class));

//        getList();
//        getListDone();

//        if(AppController.getInstance().isFirstRun()) {
//            if(AppController.getInstance().getUser() == null) {
//                getOldUser();
//            }else{
//
//            }
//        }else{
//            getList();
//        }

    }

//    private void getList(){
//        Call<List<ListItem>> listCall = AppController.getInstance().getFourAPI().getLists();
//        listCall.enqueue(new Callback<List<ListItem>>() {
//            @Override
//            public void onResponse(Call<List<ListItem>> call, Response<List<ListItem>> response) {
//                if(response.isSuccess()){
//                    Log.d("TAG", "getList successful");
//                    AppController.getInstance().setListItems(response.body());
//                    getListDone();
//                }else {
//                    Log.d("TAG", "getList error");
//                    FourAPIError error = FourAPIError.parseError(response);
//                    String s = error.getMessage();
//                    int c = error.getStatus();
//                    int a = error.hashCode();
//                    getListFailed();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<ListItem>> call, Throwable t) {
//                Log.d("TAG", "getList fail");
//                Log.d(TAG, t.getMessage());
//                getListFailed();
//            }
//        });
//    }
//
//    private void getOldUser() {
//        User user = MigrationHelper.GetLastUser(this);
//        AppController.getInstance().setAccessToken(user.getUserToken());
//        if (user != null) {
//            Call<User> call = AppController.getInstance().getFourAPI()
//                    .getMe(AppController.getInstance().getAccessToken());
//            call.enqueue(new Callback<User>() {
//                @Override
//                public void onResponse(Call<User> call, Response<User> response) {
//                    if (response.isSuccess() && response.body() != null) {
//                        User user = response.body();
//                        AppController.getInstance().setUser(user);
//                        getList();
//                    } else {
//                        getOldUserFailed();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<User> call, Throwable t) {
//                    getOldUserFailed();
//                }
//            });
//        }
//    }
//
//    private void getOldUserFailed(){
//
//    }
//
//    private void getListFailed(){
//
//    }
//
//    private void getListDone(){
////        startActivity(new Intent(this, IntroActivity.class));
//        long diff = new Date().getTime() - startDate.getTime();
//        diff = 3000 - diff;
//        if(diff < 0)
//            diff = 0;
//        imageView.postDelayed(nextRunnable, diff);
//    }
//
//    private Runnable nextRunnable = new Runnable() {
//        @Override
//        public void run() {
//            if (AppController.getInstance().getUser() == null) {
//                startActivity(new Intent(SplashActivity.this, IntroActivity.class));
//            } else {
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//            }
//            finish();
//        }
//    };

}
