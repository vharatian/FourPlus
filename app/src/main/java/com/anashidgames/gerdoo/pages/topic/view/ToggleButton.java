package com.anashidgames.gerdoo.pages.topic.view;

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
public class ToggleButton extends LinearLayout {

    private TextView textView;
    private ImageView iconView;
    private View lineView;
    private boolean state = true;

    public ToggleButton(Context context) {
        super(context);
        init(context);
    }

    public ToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ToggleButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_toggle, this);

        textView = (TextView)findViewById(R.id.textView);
        iconView = (ImageView)findViewById(R.id.iconView);
        lineView = findViewById(R.id.lineView);
        setState(true);
    }

    public void setData(int textResource, int iconResource){
        textView.setText(textResource);
        iconView.setImageResource(iconResource);
    }

    public void toggle(){
        setState(!state);
    }


    public void setState(boolean state) {
        this.state = state;

        if (state){
            iconView.setColorFilter(getResources().getColor(R.color.topicIconEnable));
            setBackgroundResource(R.color.colorPrimary);
            lineView.setBackgroundResource(R.color.topicIconEnable);
        }else {
            iconView.setColorFilter(getResources().getColor(R.color.topicIconDisable));
            setBackgroundResource(R.color.colorPrimaryDark);
            lineView.setBackgroundResource(R.color.topicIconDisable);
        }
    }
}
