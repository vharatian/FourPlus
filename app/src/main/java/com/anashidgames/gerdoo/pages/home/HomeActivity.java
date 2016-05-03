package com.anashidgames.gerdoo.pages.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.call.CallbackWithErrorDialog;
import com.anashidgames.gerdoo.core.service.model.UserInfo;
import com.anashidgames.gerdoo.pages.FragmentContainerActivity;
import com.anashidgames.gerdoo.pages.TextActivity;
import com.anashidgames.gerdoo.pages.home.view.DrawerItemView;
import com.anashidgames.gerdoo.pages.profile.ProfileActivity;
import com.bumptech.glide.Glide;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;

public class HomeActivity extends FragmentContainerActivity {

    private static HomeActivity INSTANCE;

    public static Intent newIntent(Context context){
        return new Intent(context, HomeActivity.class);
    }

    public HomeActivity() {
        super(R.id.fragment);
    }

    private List<DrawerItemView.DrawerItem> drawerItems;

    private View logoView;
    private TextView titleView;
    private View menuIcon;

    private ImageView userPictureView;
    private TextView userNameView;
    private LinearLayout optionLayout;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        INSTANCE = this;

        initDrawer();
        initToolbar();


        changeFragment(HomeFragment.newInstance());
    }

    private void initDrawer() {
        userPictureView = (ImageView) findViewById(R.id.pictureView);
        userNameView = (TextView) findViewById(R.id.nameView);
        optionLayout = (LinearLayout) findViewById(R.id.optionLayout);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        loadUserInfo();
        initDrawerItems();
        initDrawerOptions();
    }

    private void initDrawerItems() {
        Intent rateIntent = new Intent(Intent.ACTION_EDIT);
        String packageName = getPackageName();
        rateIntent.setData(Uri.parse("bazaar://details?id=" + packageName));
        rateIntent.setPackage(packageName);

        drawerItems = Arrays.asList(
                new DrawerItemView.DrawerItem(R.string.profile, R.drawable.profile_icon, ProfileActivity.newIntent(this, null)),
                new DrawerItemView.DrawerItem(R.string.gifts, R.drawable.gifts_icon, rateIntent),
                new DrawerItemView.DrawerItem(R.string.shop, R.drawable.shop_icon, rateIntent),
                new DrawerItemView.DrawerItem(R.string.vote, R.drawable.vote_icon, rateIntent),
                new DrawerItemView.DrawerItem(R.string.about_us, R.drawable.about_us_icon, TextActivity.newIntent(this, R.string.aboutUsText))
        );
    }

    private void initDrawerOptions() {
        addLineToDrawer();
        for (DrawerItemView.DrawerItem item : drawerItems){
            DrawerItemView view = new DrawerItemView(this);
            view.setData(item);
            optionLayout.addView(view);
            addLineToDrawer();
        }
    }

    private void addLineToDrawer() {
        View view = new View(this);
        view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , (int) getResources().getDimension(R.dimen.lineHeight)));
        view.setBackgroundResource(R.color.colorPrimary);
        optionLayout.addView(view);
    }

    private void loadUserInfo() {
        Call<UserInfo> call = GerdooServer.INSTANCE.getUserInfo();
        call.enqueue(new UserInfoCallBack(this));
    }

    private void setUserInfo(UserInfo data) {
        userNameView.setText(data.getName());
        Glide.with(this).load(data.getImageUrl()).crossFade().into(userPictureView);
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
        menuIcon = findViewById(R.id.menu);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });
    }

    private void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
            drawerLayout.closeDrawer(Gravity.RIGHT);
        else
            drawerLayout.openDrawer(Gravity.RIGHT);
    }


    private class UserInfoCallBack extends CallbackWithErrorDialog<UserInfo> {
        public UserInfoCallBack(Context context) {
            super(context);
        }

        @Override
        public void handleSuccessful(UserInfo data) {
            setUserInfo(data);
        }
    }

}
