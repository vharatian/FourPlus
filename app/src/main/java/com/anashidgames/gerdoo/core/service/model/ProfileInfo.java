package com.anashidgames.gerdoo.core.service.model;

/**
 * Created by psycho on 4/16/16.
 */
public class ProfileInfo {
    public static final int FRIEND_SHIP_STATE_NON = 0;
    public static final int FRIEND_SHIP_STATE_FRIEND = 1;
    public static final int FRIEND_SHIP_STATE_NOT_FRIEND = 2;
    public static final int FRIEND_SHIP_STATE_WAITING = 3;


    private String name;
    private String imageUrl;
    private String coverUrl;
    private String followToggleUrl;
    private int friendShipState;
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
    private String userId;

    public ProfileInfo(String name, String imageUrl, String coverUrl, String followToggleUrl, int fiendShipState, String imageChangeUrl, String coverChangeUrl, Boolean online, String sampleFriendsUrl, String allFriendsUrl, String sampleGiftsUrl, String allGiftsUrl, int win, int loss, int tie, int matchesCount, String userId) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.coverUrl = coverUrl;
        this.followToggleUrl = followToggleUrl;
        this.friendShipState = fiendShipState;
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
        this.userId = userId;
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

    public int getFriendShipState() {
        return friendShipState;
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

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setFriendShipState(int friendShipState) {
        this.friendShipState = friendShipState;
    }
}
