package com.anashidgames.gerdoo.pages.home.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.PsychoImageLoader;
import com.anashidgames.gerdoo.core.service.model.CategoryTopic;
import com.anashidgames.gerdoo.core.service.model.SearchedTopic;
import com.anashidgames.gerdoo.pages.topic.TopicActivity;
import com.anashidgames.gerdoo.pages.topic.list.PsychoViewHolder;

/**
 * Created by psycho on 6/12/16.
 */
public class SearchedTopicViewHolder extends PsychoViewHolder<SearchedTopic>{

    public static PsychoViewHolder<SearchedTopic> createHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.view_search_topic, parent, false);
        return new SearchedTopicViewHolder(view);
    }

    private TextView titleView;
    private TextView categoryTitleView;
    private ImageView imageView;

    private SearchedTopic item;

    public SearchedTopicViewHolder(View itemView) {
        super(itemView);
        init(itemView);
    }

    private void init(View view) {
        titleView = (TextView) view.findViewById(R.id.titleView);
        categoryTitleView = (TextView) view.findViewById(R.id.categoryTitleView);
        imageView = (ImageView) view.findViewById(R.id.imageView);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTopicPage();
            }
        });
    }

    @Override
    public void bind(SearchedTopic item) {
        super.bind(item);
        this.item = item;

        titleView.setText(item.getTitle());
        categoryTitleView.setText(item.getCategoryTitle());
        PsychoImageLoader.loadImage(itemView.getContext(), item.getImageUrl(), R.drawable.home_topic_place_holder, imageView);
    }

    private void showTopicPage() {
        getContext().startActivity(TopicActivity.newIntent(getContext(), new CategoryTopic(item)));
    }
}
