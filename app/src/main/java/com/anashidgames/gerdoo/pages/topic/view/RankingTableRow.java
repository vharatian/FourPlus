package com.anashidgames.gerdoo.pages.topic.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.model.Rank;
import com.anashidgames.gerdoo.pages.topic.list.PsychoSettable;

import java.util.Arrays;
import java.util.List;

/**
 * Created by psycho on 3/31/16.
 */
public class RankingTableRow extends LinearLayout implements PsychoSettable<Rank>{

    private static final List<Integer> colorsResource = Arrays.asList(
            R.color.rankingBackground1,
            R.color.rankingBackground2
    );
    private TextView scoreView;
    private TextView nameView;
    private TextView rankTextView;
    private ImageView rankImageView;

    private ImageView rankChangeDirection;
    private TextView rankChangeAmount;

    private View mainLayout;

    private int myRank;


    public RankingTableRow(Context context, int myRank) {
        super(context);
        this.myRank = myRank;

        init(context);
    }

    public RankingTableRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RankingTableRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RankingTableRow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_ranking_item, this);

        scoreView = (TextView) findViewById(R.id.scoreView);
        nameView = (TextView) findViewById(R.id.nameView);
        rankTextView = (TextView) findViewById(R.id.rankTextView);
        rankImageView = (ImageView) findViewById(R.id.rankImageView);

        rankChangeAmount = (TextView) findViewById(R.id.rankChangeAmount);
        rankChangeDirection = (ImageView) findViewById(R.id.rankChangeDirection);

        mainLayout = findViewById(R.id.mainLayout);
    }

    @Override
    public void setData(Rank rank) {
        scoreView.setText("" + rank.getScore());
        nameView.setText(rank.getName());

        setRank(rank.getRank());
        setRankChange(rank.getRankChange());

        int textColor = 0;
        if (rank.getRank() != myRank) {
            mainLayout.setBackgroundResource(colorsResource.get(rank.getRank() % colorsResource.size()));
            textColor = getResources().getColor(R.color.black);
        }else {
            mainLayout.setBackgroundResource(R.color.colorPrimary);
            textColor = getResources().getColor(R.color.white);
        }

        scoreView.setTextColor(textColor);
        nameView.setTextColor(textColor);
        rankTextView.setTextColor(textColor);
    }

    private void setRank(int rank) {
        if (rank < 11){
            rankTextView.setVisibility(INVISIBLE);
            rankImageView.setVisibility(VISIBLE);

            String iconName = "rank_" + rank;
            int rankResource = getResources().getIdentifier(iconName, "drawable", getContext().getPackageName());
            rankImageView.setImageResource(rankResource);

            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rankImageView.getLayoutParams();
            if (rank < 4){
                layoutParams.setMargins(0,0,0,0);
            }else {
                int margin = (int) getResources().getDimension(R.dimen.rankingIconMargin);
                layoutParams.setMargins(0,margin,0,margin);
            }

        }else {
            rankTextView.setVisibility(VISIBLE);
            rankImageView.setVisibility(GONE);
            rankTextView.setText("" + rank);
        }
    }

    private void setRankChange(int rankChange) {
        if (rankChange == 0){
            rankChangeAmount.setVisibility(GONE);
            rankChangeDirection.setImageResource(R.drawable.equal);
        }else{
            rankChangeAmount.setVisibility(VISIBLE);
            if(rankChange < 0){
                rankChangeAmount.setTextColor(getResources().getColor(R.color.topic_decrease));
                rankChangeDirection.setImageResource(R.drawable.faling);
            }else{
                rankChangeAmount.setTextColor(getResources().getColor(R.color.topic_increase));
                rankChangeDirection.setImageResource(R.drawable.rise);
            }

            rankChangeAmount.setText(Math.abs(rankChange) + "");
        }
    }
}
