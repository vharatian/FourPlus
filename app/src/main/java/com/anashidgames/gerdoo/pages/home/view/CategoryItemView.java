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
import com.anashidgames.gerdoo.core.service.model.CategoryItem;
import com.anashidgames.gerdoo.pages.topic.TopicActivity;
import com.bumptech.glide.Glide;

/**
 * Created by psycho on 3/29/16.
 */
public class CategoryItemView extends LinearLayout{
    public CategoryItemView(Context context) {
        super(context);
        init(context);
    }

    public CategoryItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CategoryItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CategoryItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private TextView titleView;
    private ImageView imageView;

    private void init(Context context) {
        inflate(context, R.layout.view_category_item, this);

        titleView = (TextView) findViewById(R.id.title);
        imageView = (ImageView) findViewById(R.id.image);
    }

    public void setItem(CategoryItem item){
        titleView.setText(item.getTitle());
        Glide.with(getContext()).load(item.getImageUrl()).crossFade().into(imageView);
        setOnClickListener(new OpenTopicListener(item.getDataUrl()));
    }

    private class OpenTopicListener implements OnClickListener {

        private String dataUrl;

        public OpenTopicListener(String dataUrl) {
            this.dataUrl = dataUrl;
        }

        @Override
        public void onClick(View v) {
            getContext().startActivity(TopicActivity.newIntent(getContext(), dataUrl));
        }
    }
}
