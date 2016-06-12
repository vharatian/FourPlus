package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 6/12/16.
 */
public class FriendRequestParameters {

    @SerializedName("userId")
    private String userId;

    public FriendRequestParameters(String userId) {
        this.userId = userId;
    }
}
