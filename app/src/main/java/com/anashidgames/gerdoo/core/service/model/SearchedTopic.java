package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 6/12/16.
 */
public class SearchedTopic {
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("title")
    private String title;
    @SerializedName("categoryTitle")
    private String categoryTitle;
    @SerializedName("leaderBoardId")
    private String leaderBoardId;
    @SerializedName("matchMakingName")
    private String matchMakingName;
    @SerializedName("bannerUrl")
    private String bannerUrl;

    public SearchedTopic(String imageUrl, String title, String categoryTitle) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.categoryTitle = categoryTitle;
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

    public String getMatchMakingName() {
        return matchMakingName;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }
}
