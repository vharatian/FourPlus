package com.anashidgames.gerdoo.pages.game.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;

/**
 * Created by psycho on 4/30/16.
 */
public class ScoreView extends LinearLayout {

    private ImageView backgroundView;
    private TextView scoreView;
    private TextView titleView;


    public ScoreView(Context context) {
        super(context);
        init(context);
    }

    public ScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScoreView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_score, this);

        backgroundView = (ImageView) findViewById(R.id.backgroundView);
        scoreView = (TextView) findViewById(R.id.scoreView);
        titleView = (TextView) findViewById(R.id.titleView);
    }

    public void init(int titleResource, int colorResource){
        backgroundView.setColorFilter(getResources().getColor(colorResource));
        titleView.setText(titleResource);
    }

    public void setScore(String score){
        scoreView.setText(score);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = backgroundView.getMeasuredHeight();
        double marginTop = (height * 0.18);
        scoreView.setPadding(0, (int) marginTop,0,0);
    }
}
