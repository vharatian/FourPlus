package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 6/12/16.
 */
public class SearchedUser {

    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("name")
    private String name;
    @SerializedName("score")
    private int score;
    @SerializedName("userId")
    private String userId;

    public SearchedUser(String imageUrl, String name, int score) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.score = score;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getUserId() {
        return userId;
    }
}
