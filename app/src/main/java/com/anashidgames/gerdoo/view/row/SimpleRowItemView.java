package com.anashidgames.gerdoo.view.row;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.PsychoImageLoader;

/**
 * Created by psycho on 3/29/16.
 */
public class SimpleRowItemView extends LinearLayout implements RowItemView<RowItem>{


    public SimpleRowItemView(Context context) {
        super(context);
        init(context);
    }

    public SimpleRowItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimpleRowItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SimpleRowItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private TextView titleView;
    private TextView categoryTitleView;
    private ImageView imageView;
    private RowItem item;

    private void init(Context context) {
        inflate(context, R.layout.view_category_topic, this);

        titleView = (TextView) findViewById(R.id.title);
        categoryTitleView = (TextView) findViewById(R.id.categoryTitle);
        imageView = (ImageView) findViewById(R.id.image);

        setOnClickListener(new OnClickListener());
    }
    @Override
    public void setData(RowItem data) {
        this.item = data;
        titleView.setText(item.getTitle());
        PsychoImageLoader.loadImage(getContext(), item.getImageUrl(), R.drawable.home_topic_place_holder, imageView);


        if (item.getSubTitle() != null){
            categoryTitleView.setVisibility(VISIBLE);
            categoryTitleView.setText(item.getSubTitle());
        }else {
            categoryTitleView.setVisibility(GONE);
        }
    }

    private class OnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (item.getIntent() != null){
                getContext().startActivity(item.getIntent());
            }else if (item.getListener() != null){
                item.getListener().onClick(SimpleRowItemView.this, item);
            }
        }
    }
}
