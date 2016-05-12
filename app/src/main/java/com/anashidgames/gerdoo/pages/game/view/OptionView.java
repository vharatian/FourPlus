package com.anashidgames.gerdoo.pages.game.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by psycho on 5/8/16.
 */
public class OptionView extends LinearLayout {

    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_LEFT_WRONG = 2;
    public static final int STATUS_LEFT_CORRECT = 3;
    public static final int STATUS_RIGHT_WRONG = 4;
    public static final int STATUS_RIGHT_CORRECT = 5;
    public static final int STATUS_DISABLED = 6;

    public static final int HIDE = 0;


    private GifImageView leftStatusIcon;
    private TextView titleView;
    private GifImageView rightStatusIcon;


    public OptionView(Context context) {
        super(context);
        init(context);
    }

    public OptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OptionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OptionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_option, this);

        leftStatusIcon = (GifImageView) findViewById(R.id.leftStatusIcon);
        rightStatusIcon = (GifImageView) findViewById(R.id.rightStatusIcon);
        titleView = (TextView) findViewById(R.id.titleView);
    }

    public void setTitle(String title){
        titleView.setText(title);
        setStatus(STATUS_NORMAL);
    }

    public void setStatus(int status){
        int backgroundResource = R.drawable.gray_button_background_normal;
        switch (status){
            case STATUS_NORMAL:
                setStatusIconResource(leftStatusIcon, HIDE);
                setStatusIconResource(rightStatusIcon, HIDE);
                leftStatusIcon.setVisibility(INVISIBLE);
                rightStatusIcon.setVisibility(INVISIBLE);
                backgroundResource = R.drawable.gray_button_background_normal;
                break;
            case STATUS_LEFT_WRONG:
                setStatusIconResource(leftStatusIcon, R.drawable.cross);
                backgroundResource = R.drawable.gray_button_background_invalid;
                break;
            case STATUS_LEFT_CORRECT:
                setStatusIconResource(leftStatusIcon, R.drawable.check_animation);
                backgroundResource = R.drawable.gray_button_background_valid;
                break;
            case STATUS_RIGHT_WRONG:
                setStatusIconResource(rightStatusIcon, R.drawable.cross);
                backgroundResource = R.drawable.gray_button_background_invalid;
                break;
            case STATUS_RIGHT_CORRECT:
                setStatusIconResource(rightStatusIcon, R.drawable.check_animation);
                backgroundResource = R.drawable.gray_button_background_valid;
                break;
            case STATUS_DISABLED:
                backgroundResource = R.drawable.gray_button_background_disabled;
        }

        titleView.setBackgroundResource(backgroundResource);

    }

    private void setStatusIconResource(GifImageView icon, int resource) {
        if(resource != HIDE ){
            icon.setVisibility(VISIBLE);
            icon.setImageResource(resource);
        }else{
            icon.setVisibility(INVISIBLE);
        }
    }
}
