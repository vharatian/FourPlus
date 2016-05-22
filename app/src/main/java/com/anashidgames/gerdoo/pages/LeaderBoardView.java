package com.anashidgames.gerdoo.pages;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.call.ConverterCall;
import com.anashidgames.gerdoo.core.service.model.Rank;
import com.anashidgames.gerdoo.pages.topic.list.PsychoAdapter;
import com.anashidgames.gerdoo.pages.topic.list.PsychoDataProvider;
import com.anashidgames.gerdoo.pages.topic.list.PsychoListResponse;
import com.anashidgames.gerdoo.pages.topic.list.PsychoViewHolder;
import com.anashidgames.gerdoo.pages.topic.view.RankingFooter;
import com.anashidgames.gerdoo.pages.topic.view.RankingTableRow;

import java.util.List;

import retrofit2.Call;

/**
 * Created by psycho on 5/17/16.
 */
public class LeaderBoardView extends FrameLayout {

    public static final int GENERAL_RANKING_PAGE = 0;
    public static final int FOLLOWING_RANKING_PAGE = 1;
    public static final int MY_RANKING_RANKING_PAGE = 2;
    private static final int NO_PAGE = -1;

    private RecyclerView recyclerView;
    private int currentPage;
    private Call<List<Rank>> leaderBoardCall;
    private RankingFooter footer;
    private View myRankButton;

    private Activity activity;
    private String leaderBoardId;

    public LeaderBoardView(Context context) {
        super(context);
        init(context);
    }

    public LeaderBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LeaderBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LeaderBoardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_leader_board, this);

        initRecyclerView();
        initFooter();
        initMyRankButton();

        setPage(GENERAL_RANKING_PAGE);
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    private void setPage(int page) {
        if (currentPage == page)
            return;

        currentPage = page;

        recyclerView.setAdapter(new RankingAdapter(activity, page));

        if (page == MY_RANKING_RANKING_PAGE)
            myRankButton.setVisibility(View.GONE);
        else
            myRankButton.setVisibility(View.VISIBLE);
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

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setLeaderBoardId(String leaderBoardId) {
        this.leaderBoardId = leaderBoardId;
    }

    public void reload() {
        int page = currentPage;
        currentPage = NO_PAGE;
        setPage(page);
    }

    private class LeaderBoardDataProvider<T> extends PsychoDataProvider<Rank> {

        private int type;
        private int page = 0;

        public LeaderBoardDataProvider(Context context, int type) {
            super(context);
            this.type = type;
        }

        @Override
        protected Call<PsychoListResponse<Rank>> callServer(String nextPage) {
            if (type == MY_RANKING_RANKING_PAGE) {
                leaderBoardCall = GerdooServer.INSTANCE.getAroundMe(leaderBoardId);
            }else {
                leaderBoardCall = GerdooServer.INSTANCE.getTopPlayers(leaderBoardId, page);
            }

            return new ConverterCall<PsychoListResponse<Rank>, List<Rank>>(leaderBoardCall) {
                @Override
                public PsychoListResponse<Rank> convert(List<Rank> data) {
                    page ++;
                    String nextPage = "" + page;
                    if (type == MY_RANKING_RANKING_PAGE || data == null || data.isEmpty()){
                        nextPage = null;
                    }
                    return new PsychoListResponse<>(data, nextPage);
                }
            };
        }
    }

    private class RankingAdapter extends PsychoAdapter<Rank> {
        public RankingAdapter(Activity activity, int page) {
            super(activity, new LeaderBoardDataProvider<Rank>(activity, page));
        }

        @Override
        public PsychoViewHolder<Rank> createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
            RankingTableRow row = new RankingTableRow(getContext());
            row.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new PsychoViewHolder<>(row);
        }
    }

    private class InnerPageSelectedListener implements RankingFooter.PageSelectedListener {
        @Override
        public void pageSelected(int index) {
            setPage(index);
        }
    }
}
