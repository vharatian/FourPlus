package com.anashidgames.gerdoo.pages.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.PsychoImageLoader;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.call.CallbackWithErrorDialog;
import com.anashidgames.gerdoo.core.service.model.server.ChangeImageResponse;
import com.anashidgames.gerdoo.core.service.model.server.FriendRequestResponse;
import com.anashidgames.gerdoo.core.service.model.ProfileInfo;
import com.anashidgames.gerdoo.pages.profile.view.FriendsRow;
import com.anashidgames.gerdoo.pages.profile.view.GiftsRow;
import com.anashidgames.gerdoo.view.chart.pie.PieChart;
import com.anashidgames.gerdoo.view.chart.pie.PieChartItem;

import retrofit2.Call;

public class ProfileActivity extends AppCompatActivity {

    public static final String USER_ID = "userId";
    private static final int REQUEST_IMAGE_PICK_PROFILE_IMAGE = 3456;
    private static final int PICK_COVER = 5678;


    public static Intent newIntent(Context context, String userId) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(USER_ID, userId);
        return intent;
    }

    private String userId;
    private ProfileInfo info;

    private View progressView;
    private View mainLayout;

    private ImageView profilePictureView;

    private TextView nameView;
    private ImageView onlineStatusView;

    private PieChart statusChart;

    private FriendsRow friendsRow;
    private GiftsRow giftsRow;

    private View requestLayout;
    private ImageView requestIcon;
    private TextView requestTextView;

    private ProgressDialog progressDialog;
    private Call<ChangeImageResponse> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userId = getIntent().getStringExtra(USER_ID);
        if (userId == null || userId.isEmpty())
            userId = null;

        initViews();

        loadData(userId);
    }

    private void initViews() {
        progressView = findViewById(R.id.progress);
        mainLayout = findViewById(R.id.mainLayout);

        initToolbar();
        initUserInfoViews();
        initStatusChart();

        friendsRow = (FriendsRow) findViewById(R.id.friendRow);
        giftsRow = (GiftsRow) findViewById(R.id.giftsRow);

        initProgressDialog();
    }

    private void initToolbar() {
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initStatusChart() {
        statusChart = (PieChart) findViewById(R.id.statusChart);
        statusChart.setCenterColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void initUserInfoViews() {
        profilePictureView = (ImageView) findViewById(R.id.profilePictureView);
        profilePictureView.setOnClickListener(new ImageChooser(REQUEST_IMAGE_PICK_PROFILE_IMAGE));

        nameView = (TextView) findViewById(R.id.nameView);
        onlineStatusView = (ImageView) findViewById(R.id.onlineStatusView);

        requestLayout = findViewById(R.id.requestLayout);
        requestIcon = (ImageView) findViewById(R.id.requestIcon);
        requestTextView = (TextView) findViewById(R.id.requestTextView);
        requestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRequest();
            }
        });
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.pleaseWait));
        progressDialog.setTitle(getString(R.string.changeImage));
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancelRequesting();
            }
        });
    }

    private void cancelRequesting() {
        if (progressDialog != null)
            progressDialog.dismiss();

        if (call != null)
            call.cancel();
    }

    private void chooseImage(int requestCode) {
        Intent pickPhotoIntent = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhotoIntent.setType("image/*");
        startActivityForResult(pickPhotoIntent, requestCode);
    }

    private void doRequest() {
        int state = info.getFriendShipState();
        if (state == ProfileInfo.FRIEND_SHIP_STATE_NON){
            startActivity(EditProfileActivity.newIntent(this));
        }else {
            requestLayout.setEnabled(false);
            if (state == ProfileInfo.FRIEND_SHIP_STATE_FRIEND){
                Call<FriendRequestResponse> call = GerdooServer.INSTANCE.unfriendRequest(info.getUserId());
                call.enqueue(new FriendRequestCallback(this));
            }else if (state == ProfileInfo.FRIEND_SHIP_STATE_NOT_FRIEND){
                Call<FriendRequestResponse> call = GerdooServer.INSTANCE.friendRequest(info.getUserId());
                call.enqueue(new FriendRequestCallback(this));
            }
        }
    }

    private void loadData(String userId) {
        mainLayout.setVisibility(View.GONE);
        progressView.setVisibility(View.VISIBLE);

        Call<ProfileInfo> call = GerdooServer.INSTANCE.getProfile(userId);
        call.enqueue(new DataCallBack(this));
    }

    private void setInfo(ProfileInfo info) {
        this.info = info;
        progressView.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);

        setImages();

        setName(info);
        setRequestLayoutState();

        setStatusChartInfo(info);

        friendsRow.setUserId(userId);
        giftsRow.setUserId(userId);
    }

    private void setName(ProfileInfo info) {
        nameView.setText(info.getName());
        if(info.getOnline() == null){
            onlineStatusView.setVisibility(View.GONE);
        }else{
            onlineStatusView.setVisibility(View.VISIBLE);
            int colorCode;
            if (info.getOnline())
                colorCode = R.color.green;
            else
                colorCode = R.color.red;

            onlineStatusView.setColorFilter(getResources().getColor(colorCode));
        }
    }

    private void setStatusChartInfo(ProfileInfo info) {
        statusChart.addData(new PieChartItem(getString(R.string.loss), info.getLoss(), getResources().getColor(R.color.red)));
        statusChart.addData(new PieChartItem(getString(R.string.tie), info.getTie(), getResources().getColor(R.color.yellow)));
        statusChart.addData(new PieChartItem(getString(R.string.win), info.getWin(), getResources().getColor(R.color.green)));

        statusChart.setBoldCenterText("" + info.getMatchesCount());
        statusChart.setSmallCenterText(getString(R.string.playedCount));
    }

    private void setImages() {
        PsychoImageLoader.loadImage(this, info.getImageUrl(), R.drawable.user_image_place_holder, profilePictureView);
    }

    private void setRequestLayoutState() {
        int state = info.getFriendShipState();
        if (state == ProfileInfo.FRIEND_SHIP_STATE_NON){
            requestIcon.setImageResource(R.drawable.settings_icon);
            requestTextView.setText(getString(R.string.changeProfile));
            requestLayout.setBackgroundResource(R.drawable.gray_background);
        }else {
            if (state == ProfileInfo.FRIEND_SHIP_STATE_FRIEND){
                requestIcon.setImageResource(R.drawable.followed);
                requestTextView.setText(R.string.followed);
                requestLayout.setBackgroundResource(R.drawable.followed_background);
            }else if (state == ProfileInfo.FRIEND_SHIP_STATE_NOT_FRIEND){
                requestIcon.setImageResource(R.drawable.add);
                requestTextView.setText(R.string.follow);
                requestLayout.setBackgroundResource(R.drawable.gray_background);
            }else if(state == ProfileInfo.FRIEND_SHIP_STATE_WAITING){
                requestIcon.setImageDrawable(null);
                requestTextView.setText(R.string.waiting);
                requestLayout.setBackgroundResource(R.drawable.gray_background);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((requestCode == PICK_COVER || requestCode == REQUEST_IMAGE_PICK_PROFILE_IMAGE) && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            uploadImage(requestCode, selectedImage);
        }else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImage(int imageType, Uri selectedImage) {
        progressDialog.show();
        String url;
        if (imageType == PICK_COVER)
            url = info.getCoverChangeUrl();
        else
            url = info.getImageChangeUrl();

        call = GerdooServer.INSTANCE.changeImage(url, selectedImage);
        call.enqueue(new ImageChangeCallBack(this, imageType));
    }

    private class DataCallBack extends CallbackWithErrorDialog<ProfileInfo> {
        public DataCallBack(Context context) {
            super(context);
        }

        @Override
        public void handleSuccessful(ProfileInfo data) {
            setInfo(data);
        }
    }

    private class FriendRequestCallback extends CallbackWithErrorDialog<FriendRequestResponse> {
        public FriendRequestCallback(Context context) {
            super(context);
        }

        @Override
        public void handleSuccessful(FriendRequestResponse data) {
            if (data.isDone()){
                int newState;
                if (info.getFriendShipState() == ProfileInfo.FRIEND_SHIP_STATE_FRIEND){
                    newState = ProfileInfo.FRIEND_SHIP_STATE_NOT_FRIEND;
                }else {
                    newState = ProfileInfo.FRIEND_SHIP_STATE_WAITING;
                }

                info.setFriendShipState(newState);
                setRequestLayoutState();
            }else{
                Toast.makeText(ProfileActivity.this, R.string.couldNotPerform, Toast.LENGTH_SHORT);
            }
        }

        @Override
        protected void postExecution() {
            super.postExecution();

            requestLayout.setEnabled(true);
        }
    }

    private class ImageChooser implements View.OnClickListener {
        private final int requestCode;

        public ImageChooser(int requestCode) {
            this .requestCode = requestCode;
        }

        @Override
        public void onClick(View v) {
            chooseImage(requestCode);
        }
    }

    private class ImageChangeCallBack extends CallbackWithErrorDialog<ChangeImageResponse> {
        private final int imageType;

        public ImageChangeCallBack(Context context, int imageType) {
            super(context);
            this.imageType = imageType;
        }

        @Override
        protected void postExecution() {
            super.postExecution();
            cancelRequesting();
        }

        @Override
        public void handleSuccessful(ChangeImageResponse data) {
            if (!data.isDone()) {
                Toast.makeText(ProfileActivity.this, R.string.couldNotPerform, Toast.LENGTH_SHORT);
                return;
            }

            if (imageType == PICK_COVER){
                info.setCoverUrl(data.getNewUrl());
            }else{
                info.setImageUrl(data.getNewUrl());
            }
            setImages();
        }
    }
}
