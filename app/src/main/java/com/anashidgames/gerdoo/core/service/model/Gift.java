package com.anashidgames.gerdoo.core.service.model;

/**
 * Created by psycho on 4/17/16.
 */
public class Gift {
    private String name;
    private String imageUrl;

    public Gift(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }


}
