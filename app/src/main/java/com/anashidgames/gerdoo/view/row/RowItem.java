package com.anashidgames.gerdoo.view.row;

import android.content.Intent;
import android.view.View;

/**
 * Created by psycho on 4/17/16.
 */
public class RowItem {
    private String title;
    private String subTitle;
    private String imageUrl;

    private Intent intent;
    private OnClickListener listener;

    public RowItem(String title, String subTitle, String imageUrl) {
        this.title = title;
        this.subTitle = subTitle;
        this.imageUrl = imageUrl;
    }

    public RowItem(String title, String subTitle, String imageUrl, Intent intent) {
        this.title = title;
        this.subTitle = subTitle;
        this.imageUrl = imageUrl;
        this.intent = intent;
    }

    public RowItem(String title, String subTitle, String imageUrl, OnClickListener listener) {
        this.title = title;
        this.subTitle = subTitle;
        this.imageUrl = imageUrl;
        this.listener = listener;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Intent getIntent() {
        return intent;
    }

    public OnClickListener getListener() {
        return listener;
    }

    public interface OnClickListener {
        void onClick(View v, RowItem item);
    }
}
