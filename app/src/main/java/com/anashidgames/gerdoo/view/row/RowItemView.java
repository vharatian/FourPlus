package com.anashidgames.gerdoo.view.row;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.bumptech.glide.Glide;

/**
 * Created by psycho on 3/29/16.
 */
public class RowItemView extends LinearLayout{
    public RowItemView(Context context) {
        super(context);
        init(context);
    }

    public RowItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RowItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RowItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private TextView titleView;
    private TextView categoryTitleView;
    private ImageView imageView;

    private void init(Context context) {
        inflate(context, R.layout.view_category_topic, this);

        titleView = (TextView) findViewById(R.id.title);
        categoryTitleView = (TextView) findViewById(R.id.categoryTitle);
        imageView = (ImageView) findViewById(R.id.image);
    }

    public void setItem(RowItem item){
        titleView.setText(item.getTitle());
        Glide.with(getContext()).load(item.getImageUrl()).placeholder(R.drawable.home_topic_place_holder).crossFade().into(imageView);
        if (item.getSubTitle() != null){
            categoryTitleView.setVisibility(VISIBLE);
            categoryTitleView.setText(item.getSubTitle());
        }else {
            categoryTitleView.setVisibility(GONE);
        }
        setOnClickListener(new ActivityStarter(item.getIntent()));
    }

    private class ActivityStarter implements OnClickListener {

        private Intent intent;

        public ActivityStarter(Intent intent) {
            this.intent = intent;
        }

        @Override
        public void onClick(View v) {
            getContext().startActivity(intent);
        }
    }
}
