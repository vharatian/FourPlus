package com.anashidgames.gerdoo.pages.topic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.call.CallbackWithErrorDialog;
import com.anashidgames.gerdoo.core.service.model.CategoryTopic;
import com.anashidgames.gerdoo.core.service.model.Rank;
import com.anashidgames.gerdoo.core.service.model.server.LeaderBoardResponse;
import com.anashidgames.gerdoo.pages.GerdooActivity;
import com.anashidgames.gerdoo.pages.game.GameActivity;
import com.anashidgames.gerdoo.pages.topic.list.PsychoViewHolder;
import com.anashidgames.gerdoo.pages.topic.view.RankingFooter;
import com.anashidgames.gerdoo.pages.topic.view.RankingTableRow;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class TopicActivity extends GerdooActivity {

    public static final String TITLE = "title";
    public static final String BANNER_URL = "bannerUrl";

    public static final int GENERAL_RANKING_PAGE = 0;
    public static final int FOLLOWING_RANKING_PAGE = 1;
    public static final int MY_RANKING_RANKING_PAGE = 2;
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

    Call<LeaderBoardResponse> leaderBoardCall;


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
        leaderBoardId = bundle.getString(LEADER_BOARD_ID);
        title = bundle.getString(TITLE);
        bannerUrl = bundle.getString(BANNER_URL);
        matchMakingName = bundle.getString(MATCH_MAKING_NAME);
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

        adapter = new RankingAdapter();
        recyclerView.setAdapter(adapter);

        setPage(GENERAL_RANKING_PAGE);
    }

    private void setPage(int page) {
        if (currentPage == page)
            return;

        currentPage = page;


        if (leaderBoardCall != null) {
            leaderBoardCall.cancel();
            leaderBoardCall = null;
        }

        if (page == MY_RANKING_RANKING_PAGE) {
            leaderBoardCall = GerdooServer.INSTANCE.getAroundMe(leaderBoardId);
            leaderBoardCall.enqueue(new LeaderBoardCallBack(this));
        }else {
            leaderBoardCall = GerdooServer.INSTANCE.getTopPlayers(leaderBoardId);
            leaderBoardCall.enqueue(new LeaderBoardCallBack(this));
        }

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

    private class RankingAdapter extends RecyclerView.Adapter<PsychoViewHolder<Rank>> {

        private List<Rank> ranks = new ArrayList<>();
        private List<RankingTableRow> rows = new ArrayList<>();
        private int myRank = 0;

        @Override
        public PsychoViewHolder<Rank> onCreateViewHolder(ViewGroup parent, int viewType) {
            RankingTableRow row = new RankingTableRow(TopicActivity.this, myRank);
            rows.add(row);
            row.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new PsychoViewHolder<>(row);
        }

        @Override
        public void onBindViewHolder(PsychoViewHolder<Rank> holder, int position) {
            holder.bind(ranks.get(position));
        }

        @Override
        public int getItemCount() {
            return ranks.size();
        }

        public void setRanks(List<Rank> ranks){
            if (ranks == null) {
                ranks = new ArrayList<>();
            }

            this.ranks = ranks;
            notifyDataSetChanged();
        }

        public void setMyRank(int myRank) {
            this.myRank = myRank;

            for(RankingTableRow row : rows){
                row.setMyRank(myRank);
            }
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

    private class LeaderBoardCallBack extends CallbackWithErrorDialog<LeaderBoardResponse> {

        public LeaderBoardCallBack(Context context) {
            super(context);
        }

        @Override
        public void handleSuccessful(LeaderBoardResponse data) {
            adapter.setMyRank(data.getMyRank());
            adapter.setRanks(data.getRanks());
        }
    }
}