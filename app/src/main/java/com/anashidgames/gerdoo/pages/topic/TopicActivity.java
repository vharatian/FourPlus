package com.anashidgames.gerdoo.pages.topic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.model.CategoryTopic;
import com.anashidgames.gerdoo.core.service.model.Rank;
import com.anashidgames.gerdoo.pages.GerdooActivity;
import com.anashidgames.gerdoo.pages.game.UserMatchingActivity;
import com.anashidgames.gerdoo.pages.topic.list.PsychoAdapter;
import com.anashidgames.gerdoo.pages.topic.list.PsychoDataProvider;
import com.anashidgames.gerdoo.pages.topic.list.PsychoListResponse;
import com.anashidgames.gerdoo.pages.topic.list.PsychoViewHolder;
import com.anashidgames.gerdoo.pages.topic.view.RankingFooter;
import com.anashidgames.gerdoo.pages.topic.view.RankingTableRow;
import com.bumptech.glide.Glide;

import retrofit2.Call;

public class TopicActivity extends GerdooActivity {

    public static final String TITLE = "title";
    public static final String BANNER_URL = "bannerUrl";
    public static final String GENERAL_RANKING_URL = "generalRankingUrl";
    public static final String FOLLOWING_RANKING_URL = "followingRankingUrl";
    public static final String MY_RANKING_URL = "MyRankingUrl";
    public static final String MY_RANK = "MyRank";

    public static final int GENERAL_RANKING_PAGE = 0;
    public static final int FOLLOWING_RANKING_PAGE = 1;
    public static final int MY_RANKING_RANKING_PAGE = 2;

    public static Intent newIntent(Context context, CategoryTopic topic) {

        Intent intent = new Intent(context, TopicActivity.class);
        intent.putExtra(GENERAL_RANKING_URL, topic.getGeneralRankingUrl());
        intent.putExtra(FOLLOWING_RANKING_URL, topic.getFollowingRankingUrl());
        intent.putExtra(MY_RANKING_URL, topic.getMyRankingUrl());
        intent.putExtra(TITLE, topic.getTitle());
        intent.putExtra(BANNER_URL, topic.getBannerUrl());
        intent.putExtra(MY_RANK, topic.getMyRank());
        return intent;
    }

    private String generalRankingUrl;
    private String followingRankingUrl;
    private String title;
    private String bannerUrl;
    private String myRankingUrl;
    private int myRank;

    private RecyclerView recyclerView;
    private RankingAdapter adapter;
    private RankingFooter footer;
    private View myRankButton;
    private AppBarLayout appBar;
    private Toolbar toolbar;
    private View titleBar;
    private TextView toolbarTitleView;
    private TextView titleView;
    private ImageView backButton;

    private int currentPage = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        initData();

        initMyRankButton();
        initToolbar();
        initRecyclerView();
        initFooter();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        generalRankingUrl = bundle.getString(GENERAL_RANKING_URL);
        followingRankingUrl = bundle.getString(FOLLOWING_RANKING_URL);
        myRankingUrl = bundle.getString(MY_RANKING_URL);
        title = bundle.getString(TITLE);
        bannerUrl = bundle.getString(BANNER_URL);
        myRank = bundle.getInt(MY_RANK);
    }

    private void initMyRankButton() {
        myRankButton = findViewById(R.id.myRankButton);
        myRankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPage(MY_RANKING_RANKING_PAGE);
                footer.disableAll();
            }
        });
    }

    private void initFooter() {
        footer = (RankingFooter) findViewById(R.id.rankingFooter);
        footer.setPageSelectedListener(new InnerPageSelectedListener());
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        setPage(GENERAL_RANKING_PAGE);
    }

    private void setPage(int page) {
        if (currentPage == page)
            return;

        currentPage = page;

        String dataUrl = null;
        if (page == GENERAL_RANKING_PAGE)
            dataUrl = generalRankingUrl;
        else if (page == FOLLOWING_RANKING_PAGE)
            dataUrl = followingRankingUrl;
        else if (page == MY_RANKING_RANKING_PAGE)
            dataUrl = myRankingUrl;

        if (adapter != null)
            adapter.cancelLoading();
        adapter = new RankingAdapter(this, dataUrl);
        recyclerView.setAdapter(adapter);

        if (page == MY_RANKING_RANKING_PAGE)
            myRankButton.setVisibility(View.GONE);
        else
            myRankButton.setVisibility(View.VISIBLE);
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
        Glide.with(this).load(bannerUrl).crossFade().into(bannerView);


        findViewById(R.id.playButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(UserMatchingActivity.newIntent(TopicActivity.this));
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

    private class RankingDataProvider extends PsychoDataProvider<Rank> {

        private GerdooServer server;
        private String rankingUrl;

        public RankingDataProvider(Context context, String rankingUrl) {
            super(context);
            server = new GerdooServer();
            this.rankingUrl = rankingUrl;
        }

        @Override
        protected Call<PsychoListResponse<Rank>> callServer(String nextPage) {
            if (nextPage == null)
                nextPage = rankingUrl;

            return server.getRanking(nextPage);
        }
    }

    private class RankingAdapter extends PsychoAdapter<Rank> {

        public RankingAdapter(Activity activity, String url) {
            super(activity, new RankingDataProvider(activity, url));
        }

        @Override
        public PsychoViewHolder<Rank> createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
            RankingTableRow view = new RankingTableRow(TopicActivity.this, myRank);
            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new PsychoViewHolder<>(view);
        }
    }

    private class InnerPageSelectedListener implements RankingFooter.PageSelectedListener {
        @Override
        public void pageSelected(int index) {
            setPage(index);
        }
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