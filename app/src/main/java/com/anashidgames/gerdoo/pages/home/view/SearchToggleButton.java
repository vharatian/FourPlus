package com.anashidgames.gerdoo.pages.home.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;

/**
 * Created by psycho on 4/3/16.
 */
public class SearchToggleButton extends LinearLayout {

    private TextView textView;
    private boolean state = true;

    public SearchToggleButton(Context context) {
        super(context);
        init(context);
    }

    public SearchToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SearchToggleButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_search_toggle, this);

        textView = (TextView)findViewById(R.id.textView);
        setState(true);
    }

    public void setData(int textResource){
        textView.setText(textResource);
    }

    public void toggle(){
        setState(!state);
    }


    public void setState(boolean state) {
        this.state = state;

        if (state){
            setBackgroundResource(R.color.colorPrimary);
        }else {
            setBackgroundResource(R.color.colorPrimaryDark);
        }
    }
}
