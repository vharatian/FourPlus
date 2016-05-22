package com.anashidgames.gerdoo.core;


import android.content.Context;
import android.widget.ImageView;

import com.anashidgames.gerdoo.pages.topic.TopicActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by psycho on 5/21/16.
 */
public class PsychoImageLoader {

    public static void loadImage(Context context, String url, int placeholder, ImageView target){
        if (url == null || url.isEmpty()){
            url = "http://kajdkfhgjajdfg.com";
        }

        Picasso
                .with(context)
                .load(url)
                .placeholder(placeholder)
                .into(target);
    }

    public static void loadImage(Context context, String url, ImageView target) {
        if (url == null || url.isEmpty()){
            url = "http://kajdkfhgjajdfg.com";
        }

        Picasso
                .with(context)
                .load(url)
                .into(target);
    }
}
