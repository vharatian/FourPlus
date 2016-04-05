package com.anashidgames.gerdoo.pages.topic.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.pages.topic.TopicActivity;

/**
 * Created by psycho on 4/3/16.
 */
public class RankingFooter extends LinearLayout {

    private PageSelectedListener pageSelectedListener;
    private ToggleButton generalRanking;
    private ToggleButton followingRanking;
    private View generalRankingArrow;
    private View followingRankingArrow;

    public RankingFooter(Context context) {
        super(context);
        init(context);
    }

    public RankingFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RankingFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RankingFooter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_ranking_footer, this);

        generalRanking = (ToggleButton) findViewById(R.id.generalRanking);
        followingRanking = (ToggleButton) findViewById(R.id.followingRanking);

        generalRankingArrow = findViewById(R.id.generalRankingArrow);
        followingRankingArrow = findViewById(R.id.followingRankingArrow);

        generalRanking.setData(R.string.generalRanking, R.drawable.general_ranking);
        followingRanking.setData(R.string.followerRanking, R.drawable.fllowing_ranking);

        ToggleListener listener = new ToggleListener(generalRanking);
        generalRanking.setOnClickListener(listener);
        followingRanking.setOnClickListener(listener);

        reset();
    }

    private void reset() {
        generalRanking.setState(true);
        generalRankingArrow.setVisibility(VISIBLE);
        followingRanking.setState(false);
        followingRankingArrow.setVisibility(INVISIBLE);
    }

    private void changeGenereralButtonState(boolean state){
        generalRanking.setState(state);
        if(state){
            generalRankingArrow.setVisibility(VISIBLE);
        }else{
            generalRankingArrow.setVisibility(INVISIBLE);
        }
    }

    private void changeFollowingButtonState(boolean state){
        followingRanking.setState(state);
        if(state){
            followingRankingArrow.setVisibility(VISIBLE);
        }else{
            followingRankingArrow.setVisibility(INVISIBLE);
        }
    }

    private void select(boolean general){
        if (general){
            changeGenereralButtonState(true);
            changeFollowingButtonState(false);

            pageSelectedListener.pageSelected(TopicActivity.GENERAL_RANKING_PAGE);
        }else{
            changeFollowingButtonState(true);
            changeGenereralButtonState(false);

            pageSelectedListener.pageSelected(TopicActivity.FOLLOWING_RANKING_PAGE);
        }
    }

    public void disableAll(){
        changeGenereralButtonState(false);
        changeFollowingButtonState(false);
    }

    public void setPageSelectedListener(PageSelectedListener pageSelectedListener) {
        this.pageSelectedListener = pageSelectedListener;
    }

    private class ToggleListener implements View.OnClickListener {
        private ToggleButton first;

        public ToggleListener(ToggleButton first) {
            this.first = first;
        }

        @Override
        public void onClick(View v) {
            select(v == first);
        }
    }

    public interface PageSelectedListener{
        void pageSelected(int index);
    }
}
