package com.anashidgames.gerdoo.pages.home.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.PsychoImageLoader;
import com.anashidgames.gerdoo.core.service.model.AchievementItem;
import com.anashidgames.gerdoo.pages.topic.list.PsychoViewHolder;

/**
 * Created by psycho on 6/5/16.
 */
public class AchievementItemViewHolder extends PsychoViewHolder<AchievementItem>{

    public static AchievementItemViewHolder createHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.view_achievemnt, parent, false);
        return new AchievementItemViewHolder(view);
    }

    private ImageView imageView;
    private TextView titleView;

    public AchievementItemViewHolder(View itemView) {
        super(itemView);
        init(itemView);
    }

    private void init(View view) {
        imageView = (ImageView) view.findViewById(R.id.imageView);
        titleView = (TextView) view.findViewById(R.id.titleView);
    }


    @Override
    public void bind(AchievementItem item) {
        super.bind(item);

        Context context = itemView.getContext();
        PsychoImageLoader.loadImage(context, item.getImageUrl(), R.drawable.home_topic_place_holder, imageView);
        if (item.have()){
            imageView.setColorFilter(null);
        }else {
            imageView.setColorFilter(context.getResources().getColor(R.color.gray));
        }
        titleView.setText(item.getTitle());
    }
}
