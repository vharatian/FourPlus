package com.anashidgames.gerdoo.core.service.model;

/**
 * Created by psycho on 4/17/16.
 */
public class Friend {
    private String imageUrl;
    private String name;
    private String userId;
    private boolean requesting;

    public Friend(String imageUrl, String name, String userId, boolean requesting) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.userId = userId;
        this.requesting = requesting;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isRequesting() {
        return requesting;
    }

    public void requestAnswered() {
        this.requesting = false;
    }
}
