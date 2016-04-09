package com.anashidgames.gerdoo.pages.home.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.model.CategoryTopic;
import com.anashidgames.gerdoo.pages.topic.TopicActivity;
import com.bumptech.glide.Glide;

/**
 * Created by psycho on 3/29/16.
 */
public class CategoryTopicView extends LinearLayout{
    public CategoryTopicView(Context context) {
        super(context);
        init(context);
    }

    public CategoryTopicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CategoryTopicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CategoryTopicView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

    public void setItem(CategoryTopic item, String categoryTitle, @Nullable String categoryIconUrl){
        titleView.setText(item.getTitle());
        Glide.with(getContext()).load(item.getImageUrl()).placeholder(R.drawable.home_topic_place_holder).crossFade().into(imageView);
        if (item.getCategoryTitle() != null){
            categoryTitleView.setVisibility(VISIBLE);
            categoryTitleView.setText(item.getCategoryTitle());
        }else {
            categoryTitleView.setVisibility(INVISIBLE);
        }
        setOnClickListener(new OpenTopicListener(item));
    }

    private class OpenTopicListener implements OnClickListener {

        private CategoryTopic categoryTopic;

        public OpenTopicListener(CategoryTopic categoryItem) {
            this.categoryTopic = categoryItem;
        }

        @Override
        public void onClick(View v) {
            getContext().startActivity(TopicActivity.newIntent(
                    getContext(), categoryTopic));
        }
    }
}
