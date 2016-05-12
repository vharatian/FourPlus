package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 5/9/16.
 */
public class ParticipantInfo {
    @SerializedName("userId")
    private String userId;
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("name")
    private String name;

    public String getUserId() {
        return userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }
}
