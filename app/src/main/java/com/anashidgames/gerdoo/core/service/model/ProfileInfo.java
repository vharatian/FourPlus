package com.anashidgames.gerdoo.core.service.model;

/**
 * Created by psycho on 4/16/16.
 */
public class ProfileInfo {
    private String name;
    private String imageUrl;
    private String coverUrl;
    private String followToggleUrl;
    private Boolean following;
    private String imageChangeUrl;
    private String coverChangeUrl;
    private Boolean online;
    private String sampleFriendsUrl;
    private String allFriendsUrl;
    private String sampleGiftsUrl;
    private String allGiftsUrl;
    private int win;
    private int loss;
    private int tie;
    private int matchesCount;

    public ProfileInfo(String name, String imageUrl, String coverUrl, String followToggleUrl, Boolean following, String imageChangeUrl, String coverChangeUrl, Boolean online, String sampleFriendsUrl, String allFriendsUrl, String sampleGiftsUrl, String allGiftsUrl, int win, int loss, int tie, int matchesCount) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.coverUrl = coverUrl;
        this.followToggleUrl = followToggleUrl;
        this.following = following;
        this.imageChangeUrl = imageChangeUrl;
        this.coverChangeUrl = coverChangeUrl;
        this.online = online;
        this.sampleFriendsUrl = sampleFriendsUrl;
        this.allFriendsUrl = allFriendsUrl;
        this.sampleGiftsUrl = sampleGiftsUrl;
        this.allGiftsUrl = allGiftsUrl;
        this.win = win;
        this.loss = loss;
        this.tie = tie;
        this.matchesCount = matchesCount;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getFollowToggleUrl() {
        return followToggleUrl;
    }

    public Boolean getFollowing() {
        return following;
    }

    public String getImageChangeUrl() {
        return imageChangeUrl;
    }

    public String getCoverChangeUrl() {
        return coverChangeUrl;
    }

    public Boolean getOnline() {
        return online;
    }

    public String getSampleFriendsUrl() {
        return sampleFriendsUrl;
    }

    public String getAllFriendsUrl() {
        return allFriendsUrl;
    }

    public String getSampleGiftsUrl() {
        return sampleGiftsUrl;
    }

    public String getAllGiftsUrl() {
        return allGiftsUrl;
    }

    public int getWin() {
        return win;
    }

    public int getLoss() {
        return loss;
    }

    public int getTie() {
        return tie;
    }

    public int getMatchesCount() {
        return matchesCount;
    }

    public void toggleFollow(String newToggleFollowUrl){
        if (following == null)
            return;

        this.followToggleUrl = newToggleFollowUrl;
        following = !following;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
