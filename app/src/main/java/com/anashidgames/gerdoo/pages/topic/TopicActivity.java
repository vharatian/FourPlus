package com.anashidgames.gerdoo.pages.topic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.PsychoImageLoader;
import com.anashidgames.gerdoo.core.service.model.CategoryTopic;
import com.anashidgames.gerdoo.pages.GerdooActivity;
import com.anashidgames.gerdoo.pages.LeaderBoardView;
import com.anashidgames.gerdoo.pages.game.GameActivity;

public class TopicActivity extends GerdooActivity {

    public static final String TITLE = "title";
    public static final String BANNER_URL = "bannerUrl";

    public static final String LEADER_BOARD_ID = "LeaderBoardId";
    public static final String MATCH_MAKING_NAME = "matchMakingName";


    public static Intent newIntent(Context context, CategoryTopic topic) {

        Intent intent = new Intent(context, TopicActivity.class);
        intent.putExtra(LEADER_BOARD_ID, topic.getLeaderBoardId());
        intent.putExtra(TITLE, topic.getTitle());
        intent.putExtra(BANNER_URL, topic.getBannerUrl());
        intent.putExtra(MATCH_MAKING_NAME, topic.getMatchMakingName());
        return intent;
    }

    private String leaderBoardId;
    private String title;
    private String bannerUrl;
    private String matchMakingName;


    private AppBarLayout appBar;
    private Toolbar toolbar;
    private View titleBar;
    private TextView toolbarTitleView;
    private TextView titleView;
    private ImageView backButton;
    private LeaderBoardView leaderBoardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        initData();

        initToolbar();
        initLarboardView();
    }

    private void initLarboardView() {
        leaderBoardView = (LeaderBoardView) findViewById(R.id.leaderBoardView);
        leaderBoardView.setActivity(this);
        leaderBoardView.setLeaderBoardId(leaderBoardId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadPage();
    }

    private void reloadPage() {
        leaderBoardView.reload();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        leaderBoardId = bundle.getString(LEADER_BOARD_ID);
        title = bundle.getString(TITLE);
        bannerUrl = bundle.getString(BANNER_URL);
        matchMakingName = bundle.getString(MATCH_MAKING_NAME);
    }



    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        titleBar = findViewById(R.id.titleBar);

        initTitle();
        initBackButton();
        initHeader();
    }

    private void initTitle() {
        getSupportActionBar().setTitle("");
        toolbarTitleView = (TextView) findViewById(R.id.toolbarTitleView);
        toolbarTitleView.setText(title);
        titleView = (TextView) findViewById(R.id.titleView);
        titleView.setText(title);
    }


    private void initHeader() {
        ImageView bannerView = (ImageView) findViewById(R.id.bannerView);
        PsychoImageLoader.loadImage(this, bannerUrl, bannerView);


        findViewById(R.id.playButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(GameActivity.newIntent(TopicActivity.this, matchMakingName));
            }
        });

        appBar = (AppBarLayout) findViewById(R.id.appBar);
        appBar.addOnOffsetChangedListener(new ToolbarThemListener());
    }

    private void initBackButton() {
        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



    private class ToolbarThemListener implements AppBarLayout.OnOffsetChangedListener {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            int max = appBarLayout.getTotalScrollRange();
            verticalOffset = Math.abs(verticalOffset);
            double index = (max - verticalOffset * 1.0) / (max);

            index *= 3;
            if (index > 1)
                index = 1;

            fixTitleBarPosition(verticalOffset, max);
            setTitleBarColor(index);
            setBackButtonColor(index);
            checkToolbarState(index);
        }

        private void checkToolbarState(double index) {
            if (index == 0){
                toolbar.setBackgroundColor(getBarColor(index));
                toolbarTitleView.setVisibility(View.VISIBLE);
            }else{
                toolbarTitleView.setVisibility(View.INVISIBLE);
                toolbar.setBackgroundResource(android.R.color.transparent);
            }
        }

        private void setBackButtonColor(double index) {
            float alpha;
            if (index > 0.5){
                alpha = (float) ((index - 0.5)*2);
            }else{
                alpha = 0;
            }

            backButton.setAlpha(alpha);
        }

        private void fixTitleBarPosition(int verticalOffset, int max) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) titleBar.getLayoutParams();
            int margin = max - verticalOffset;
            layoutParams.topMargin = margin;
            titleBar.requestLayout();
        }

        private void setTitleBarColor(double index) {
            titleBar.setBackgroundColor(getBarColor(index));
            titleView.setTextColor(getTitleColor(index));
        }

        private int getBarColor(double index){
            int alpha;
            if (index > 0.5) {
                alpha = 100 + (int) (155 * (index - 0.5)*2);
            }else{
                alpha = 100 + (int) (155 * (0.5 - index)*1.2);
            }

            int color = (int) (0xff * index);
            return color + color * 0x100 + color * 0x10000 + alpha * 0x1000000;
        }

        private int getTitleColor(double index){
            int color = (int) (0xff * (1 - index));
            return color + color * 0x100 + color * 0x10000 + 0xff000000;
        }
    }
}