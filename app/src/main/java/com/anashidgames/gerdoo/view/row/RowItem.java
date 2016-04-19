package com.anashidgames.gerdoo.view.row;

import android.content.Intent;

/**
 * Created by psycho on 4/17/16.
 */
public class RowItem {
    private String title;
    private String subTitle;
    private String imageUrl;

    private Intent intent;

    public RowItem(String title, String subTitle, String imageUrl, Intent intent) {
        this.title = title;
        this.subTitle = subTitle;
        this.imageUrl = imageUrl;
        this.intent = intent;
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
}
