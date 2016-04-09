package com.anashidgames.gerdoo.core.service.model;

/**
 * Created by psycho on 3/29/16.
 */
public class CategoryTopic {
    private String imageUrl;
    private String title;
    private String categoryTitle;
    private String generalRankingUrl;
    private String followingRankingUrl;
    private String myRankingUrl;
    private String bannerUrl;
    private int myRank;

    public CategoryTopic(String imageUrl, String title, String categoryTitle, String generalRankingUrl, String followingRankingUrl, String myRankingUrl, String bannerUrl, int myRank) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.categoryTitle = categoryTitle;
        this.generalRankingUrl = generalRankingUrl;
        this.followingRankingUrl = followingRankingUrl;
        this.myRankingUrl = myRankingUrl;
        this.bannerUrl = bannerUrl;
        this.myRank = myRank;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getGeneralRankingUrl() {
        return generalRankingUrl;
    }

    public String getFollowingRankingUrl() {
        return followingRankingUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public String getMyRankingUrl() {
        return myRankingUrl;
    }

    public int getMyRank() {
        return myRank;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }
}
