package com.anashidgames.gerdoo.pages.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.callback.CallbackWithErrorDialog;
import com.anashidgames.gerdoo.core.service.model.ChangeImageResponse;
import com.anashidgames.gerdoo.core.service.model.FollowToggleResponse;
import com.anashidgames.gerdoo.core.service.model.ProfileInfo;
import com.anashidgames.gerdoo.pages.profile.view.FriendsRow;
import com.anashidgames.gerdoo.pages.profile.view.GiftsRow;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;

public class ProfileActivity extends AppCompatActivity {

    public static final String USER_ID = "userId";
    private static final int REQUEST_IMAGE_PICK_PROFILE_IMAGE = 3456;
    private static final int PICK_COVER = 5678;


    public static Intent newIntent(Context context, Long userId) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(USER_ID, userId);
        return intent;
    }

    private Long userId;
    private ProfileInfo info;

    private View progressView;
    private View mainLayout;

    private ImageView coverView;
    private ImageView profilePictureView;

    private TextView nameView;
    private ImageView onlineStatusView;

    private PieChart statusChart;
    private TextView matchesCountView;

    private FriendsRow friendsRow;
    private GiftsRow giftsRow;

    private View followLayout;
    private ImageView followIcon;
    private TextView followTextView;

    private ImageView coverEditView;

    private ProgressDialog progressDialog;
    private Call<ChangeImageResponse> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userId = getIntent().getLongExtra(USER_ID, -1);
        if (userId < 0)
            userId = null;

        initViews();

        loadData(userId);
    }

    private void initViews() {
        progressView = findViewById(R.id.progress);
        mainLayout = findViewById(R.id.mainLayout);

        coverView = (ImageView) findViewById(R.id.coverView);
        profilePictureView = (ImageView) findViewById(R.id.profilePictureView);

        nameView = (TextView) findViewById(R.id.nameView);
        onlineStatusView = (ImageView) findViewById(R.id.onlineStatusView);

        statusChart = (PieChart) findViewById(R.id.statusChart);
        matchesCountView = (TextView) findViewById(R.id.matchesCountView);

        friendsRow = (FriendsRow) findViewById(R.id.friendRow);
        giftsRow = (GiftsRow) findViewById(R.id.giftsRow);

        followLayout = findViewById(R.id.followLayout);
        followIcon = (ImageView) findViewById(R.id.followIcon);
        followTextView = (TextView) findViewById(R.id.followTextView);
        followLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFollow();
            }
        });

        coverEditView = (ImageView) findViewById(R.id.coverEditButton);
        if(userId != null)
            coverEditView.setVisibility(View.GONE);
        else
            coverEditView.setVisibility(View.VISIBLE);

        coverEditView.setOnClickListener(new ImageChooser(PICK_COVER));
        profilePictureView.setOnClickListener(new ImageChooser(REQUEST_IMAGE_PICK_PROFILE_IMAGE));

        initProgressDialog();
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

    private void toggleFollow() {
        if (info.getFollowing() == null)
            return;

        followLayout.setEnabled(false);
        Call<FollowToggleResponse> call = GerdooServer.INSTANCE.toggleFollow(info.getFollowToggleUrl());
        call.enqueue(new FollowToggleCallback(this));
    }

    private void loadData(Long userId) {
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

        List<Entry> entries = Arrays.asList(
                new Entry(info.getLoss(), 0),
                new Entry(info.getTie(), 1),
                new Entry(info.getWin(), 2)
        );
        PieDataSet dataSet = new PieDataSet(entries, "");

        List<Integer> colors = Arrays.asList(
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.yellow),
                getResources().getColor(R.color.green)
        );

        dataSet.setColors(colors);

        List<String> labels = Arrays.asList("باخت", "تساوی", "برد");

        PieData pieData = new PieData(labels, dataSet);
        statusChart.setData(pieData);
        statusChart.animateY(1000);
        statusChart.setDescription("");
        statusChart.getLegend().setEnabled(false);
        statusChart.setHoleColor(getResources().getColor(android.R.color.transparent));
        statusChart.setHoleRadius(70f);

        matchesCountView.setText("" + info.getMatchesCount());

        friendsRow.setUserId(userId);
        giftsRow.setUserId(userId);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ((ViewGroup.MarginLayoutParams)profilePictureView.getLayoutParams()).topMargin =
                        coverView.getHeight() - profilePictureView.getHeight()/2;
            }
        });


        setToggleFollowState();

    }

    private void setImages() {
        Glide.with(this).load(info.getCoverUrl()).placeholder(R.drawable.cover_place_holder).crossFade().into(coverView);
        Glide.with(this).load(info.getImageUrl()).placeholder(R.drawable.user_image_place_holder).crossFade().into(profilePictureView);
    }

    private void setToggleFollowState() {
        if (info.getFollowing() == null){
            followLayout.setVisibility(View.GONE);
        }else {
            followLayout.setVisibility(View.VISIBLE);
            if (info.getFollowing()){
                followIcon.setImageResource(R.drawable.follow);
                followTextView.setText(R.string.follow);
                followTextView.setTextColor(getResources().getColor(R.color.green));
                followTextView.setBackgroundResource(R.drawable.follow_background);
            }else{
                followIcon.setImageResource(R.drawable.unfollow);
                followTextView.setText(R.string.unfollow);
                followTextView.setTextColor(getResources().getColor(R.color.red));
                followTextView.setBackgroundResource(R.drawable.unfollow_background);
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

    private class FollowToggleCallback extends CallbackWithErrorDialog<FollowToggleResponse> {
        public FollowToggleCallback(Context context) {
            super(context);
        }

        @Override
        public void handleSuccessful(FollowToggleResponse data) {
            if (data.isDone()){
                info.toggleFollow(data.getToggleUrl());
                setToggleFollowState();
            }else{
                Toast.makeText(ProfileActivity.this, R.string.couldNotPerform, Toast.LENGTH_SHORT);
            }
        }

        @Override
        protected void postExecution() {
            super.postExecution();

            followLayout.setEnabled(true);
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
