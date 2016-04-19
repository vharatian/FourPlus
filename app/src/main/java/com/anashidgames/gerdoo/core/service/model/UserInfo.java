package com.anashidgames.gerdoo.core.service.model;

/**
 * Created by psycho on 4/15/16.
 */
public class UserInfo {
    private String imageUrl;
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
