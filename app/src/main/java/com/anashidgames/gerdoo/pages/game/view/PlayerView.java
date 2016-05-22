package com.anashidgames.gerdoo.pages.game.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.PsychoImageLoader;
import com.anashidgames.gerdoo.pages.game.PlayerData;

/**
 * Created by psycho on 4/24/16.
 */
public class PlayerView extends FrameLayout {

    public static final int NO_BANNER = 0;
    public static final int WIN_BANNER = 1;
    public static final int LOOS_BANNER = 2;
    public static final int TIE_BANNER = 3;
    private ImageView avatarView;
    private TextView nameView;
    private TextView scoreView;
    private ImageView bannerView;

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
        nameView = (TextView) findViewById(R.id.nameView);
        scoreView = (TextView) findViewById(R.id.scoreView);
        bannerView = (ImageView) findViewById(R.id.bannerView);

        setBanner(NO_BANNER);
    }

    public void setData(PlayerData data){

        PsychoImageLoader.loadImage(getContext(), data.getImageUrl(), R.drawable.user_image_place_holder, avatarView);

        nameView.setText(data.getName());
        if (data.hasScore()){
            scoreView.setVisibility(VISIBLE);
            scoreView.setText("" + data.getScore());
        } else {
          scoreView.setVisibility(INVISIBLE);
        }
    }

    public void setBanner(int banner){
        if (banner == NO_BANNER){
            bannerView.setVisibility(GONE);
        }else{
            bannerView.setVisibility(VISIBLE);
            if (banner == WIN_BANNER){
                bannerView.setImageResource(R.drawable.win);
            }else if (banner == LOOS_BANNER){
                bannerView.setImageResource(R.drawable.loos);
            }else if (banner == TIE_BANNER){
                bannerView.setImageResource(R.drawable.tie);
            }
        }
    }
}
