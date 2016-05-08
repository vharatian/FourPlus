package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 3/29/16.
 */
public class CategoryTopic {

    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("title")
    private String title;
    @SerializedName("categoryTitle")
    private String categoryTitle;
    @SerializedName("leaderBoardId")
    private String leaderBoardId;
    @SerializedName("bannerUrl")
    private String bannerUrl;

    public CategoryTopic(String imageUrl, String title, String categoryTitle, String leaderBoardId, String bannerUrl) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.categoryTitle = categoryTitle;
        this.leaderBoardId = leaderBoardId;
        this.bannerUrl = bannerUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public String getLeaderBoardId() {
        return leaderBoardId;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }
}
