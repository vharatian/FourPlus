package com.anashidgames.gerdoo.core.service.model;

/**
 * Created by psycho on 4/17/16.
 */
public class Friend {
    private String imageUrl;
    private String name;
    private String userId;

    public Friend(String imageUrl, String name, String userId) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.userId = userId;
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
}
