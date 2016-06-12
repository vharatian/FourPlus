package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 6/5/16.
 */
public class AchievementItem {
    @SerializedName("title")
    private String title;
    @SerializedName("iamgeUrl")
    private String imageUrl;
    @SerializedName("have")
    private boolean have;


    public AchievementItem(String title, String imageUrl, boolean have) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.have = have;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean have() {
        return have;
    }
}
