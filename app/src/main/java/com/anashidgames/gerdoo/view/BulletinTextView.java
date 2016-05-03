package com.anashidgames.gerdoo.view;

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
public class BulletinTextView extends LinearLayout{

    private TextView descriptionView;
    private ImageView bulletView;

    public BulletinTextView(Context context) {
        super(context);
        init(context);
    }

    public BulletinTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BulletinTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BulletinTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_bulletin_textview, this);

        descriptionView = (TextView) findViewById(R.id.descriptionView);
        bulletView = (ImageView) findViewById(R.id.bulletView);
    }

    public void setData(String title, int color){
        descriptionView.setText(title);

        descriptionView.setTextColor(color);
        bulletView.setColorFilter(color);
    }
}
