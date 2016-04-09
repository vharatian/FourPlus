package com.anashidgames.gerdoo.pages.topic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        initBackButton();
        initHeader();
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
    }

    private void initBackButton() {
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
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
}