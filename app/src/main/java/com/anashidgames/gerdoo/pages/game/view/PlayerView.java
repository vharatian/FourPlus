package com.anashidgames.gerdoo.pages.game.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.pages.game.PlayerData;
import com.bumptech.glide.Glide;

/**
 * Created by psycho on 4/24/16.
 */
public class PlayerView extends FrameLayout {

    private ImageView avatarView;
    private ImageView coverView;
    private TextView nameView;
    private TextView scoreView;
    private View winnerView;

    public PlayerView(Context context) {
        super(context);
        init(context);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_player, this);

        avatarView = (ImageView) findViewById(R.id.avatarView);
        coverView = (ImageView) findViewById(R.id.coverView);
        nameView = (TextView) findViewById(R.id.nameView);
        scoreView = (TextView) findViewById(R.id.scoreView);
        winnerView = findViewById(R.id.winnerView);

        setWinner(false);
    }

    public void setData(PlayerData data){

        Glide.with(getContext()).load(data.getAvatarUrl()).placeholder(R.drawable.user_image_place_holder).crossFade().into(avatarView);
        Glide.with(getContext()).load(data.getCoverUrl()).placeholder(R.drawable.cover_place_holder).crossFade().into(coverView);

        nameView.setText(data.getName());
        if (data.hasScore()){
            scoreView.setVisibility(VISIBLE);
            scoreView.setText("" + data.getScore());
        } else {
          scoreView.setVisibility(INVISIBLE);
        }
    }

    public void setWinner(boolean isWinner){
        if (isWinner){
            winnerView.setVisibility(VISIBLE);
        }else{
            winnerView.setVisibility(GONE);
        }
    }
}
