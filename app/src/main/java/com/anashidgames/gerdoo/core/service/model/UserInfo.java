package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 4/15/16.
 */
public class UserInfo {
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("name")
    private String name;

    public UserInfo(String imageUrl, String name) {
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }
}
