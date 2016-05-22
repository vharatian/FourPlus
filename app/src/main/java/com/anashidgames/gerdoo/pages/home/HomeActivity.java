package com.anashidgames.gerdoo.pages.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.PsychoImageLoader;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.call.CallbackWithErrorDialog;
import com.anashidgames.gerdoo.core.service.model.UserInfo;
import com.anashidgames.gerdoo.core.service.model.server.ChangeImageResponse;
import com.anashidgames.gerdoo.pages.FragmentContainerActivity;
import com.anashidgames.gerdoo.pages.TextActivity;
import com.anashidgames.gerdoo.pages.home.view.DrawerItemView;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;

public class HomeActivity extends FragmentContainerActivity {

    public static final int PIC_IMAGE_REQUEST_CODE = 235;
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
    private ProgressDialog progressDialog;
    private Call<ChangeImageResponse> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        INSTANCE = this;

        initProgressDialog();

        initDrawer();
        initToolbar();


        changeFragment(HomeFragment.newInstance());
    }

    private void initProgressDialog() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.pleaseWait));
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancelRequesting();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelRequesting();
    }

    private void cancelRequesting() {
        if (progressDialog != null)
            progressDialog.dismiss();

        if (call != null)
            call.cancel();
    }

    private void initDrawer() {
        userPictureView = (ImageView) findViewById(R.id.pictureView);
        userPictureView.setOnClickListener(new ChangeImageListenner());
        userNameView = (TextView) findViewById(R.id.nameView);
        optionLayout = (LinearLayout) findViewById(R.id.optionLayout);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerLayout.addDrawerListener(new DrawerListener());

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
//                new DrawerItemView.DrawerItem(R.string.profile, R.drawable.profile_icon, ProfileActivity.newIntent(this, null)),
//                new DrawerItemView.DrawerItem(R.string.gifts, R.drawable.gifts_icon, rateIntent),
//                new DrawerItemView.DrawerItem(R.string.shop, R.drawable.shop_icon, rateIntent),
//                new DrawerItemView.DrawerItem(this, R.string.vote, R.drawable.vote_icon, rateIntent),
                new DrawerItemView.DrawerItem(this, R.string.mainLeaderBord, R.drawable.vote_icon, MainLeaderBoardActivity.newIntent(this)),
                new DrawerItemView.DrawerItem(this, R.string.about_us, R.drawable.about_us_icon, TextActivity.newIntent(this, R.string.about_us, R.string.aboutUsText))
//                new DrawerItemView.DrawerItem(this, R.string.signOut, R.drawable.sign_out, new SignOutListener())
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
        setUserInfo(null);
        Call<UserInfo> call = GerdooServer.INSTANCE.getUserInfo();
        call.enqueue(new UserInfoCallBack(this));
    }

    private void setUserInfo(UserInfo data) {
        if (data != null) {
            userNameView.setText(data.getName());
            loadImage(data.getImageUrl());
        }else {
            userNameView.setText("");
            userPictureView.setImageDrawable(null);
        }
    }

    private void loadImage(String url) {
        Log.i("psycho", "image load: " + url);
        PsychoImageLoader.loadImage(this, url, R.drawable.user_image_place_holder, userPictureView);
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

    public void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
            drawerLayout.closeDrawer(Gravity.RIGHT);
        else
            drawerLayout.openDrawer(Gravity.RIGHT);
    }

    public void closeDrawer(){
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }
    }

    private void chooseImage() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK);
        pickPhotoIntent.setType("image/*");
        startActivityForResult(pickPhotoIntent, PIC_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PIC_IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            uploadImage(selectedImage);
        }else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImage(Uri selectedImage) {
        progressDialog.show();


        try {
            GerdooServer.INSTANCE.changeImage(getContentResolver().openInputStream(selectedImage), new ImageChangeCallBack(this));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private class ImageChangeCallBack extends CallbackWithErrorDialog<ChangeImageResponse> {

        public ImageChangeCallBack(Context context) {
            super(context);
        }

        @Override
        protected void postExecution() {
            super.postExecution();
            cancelRequesting();
        }

        @Override
        public void handleSuccessful(ChangeImageResponse data) {
            cancelRequesting();
            if (!data.isDone()) {
                Toast.makeText(HomeActivity.this, R.string.couldNotPerform, Toast.LENGTH_SHORT);
            }else {
                loadImage(data.getNewUrl());
            }
        }
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

    private class SignOutListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            GerdooServer.INSTANCE.getAuthenticationManager().signOut();
        }
    }

    private class ChangeImageListenner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            closeDrawer();
            chooseImage();
        }
    }

    private class DrawerListener implements DrawerLayout.DrawerListener {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {
            loadUserInfo();
        }

        @Override
        public void onDrawerClosed(View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    }
}
