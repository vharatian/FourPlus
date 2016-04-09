package com.anashidgames.gerdoo.pages.home;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.anashidgames.gerdoo.R;

/**
 * Created by psycho on 4/5/16.
 */
public class Line extends View{
    public Line(Context context) {
        super(context);
        init(context);
    }

    public Line(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Line(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Line(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setBackgroundResource(R.drawable.line_home);
    }
}
