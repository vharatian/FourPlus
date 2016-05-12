package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 4/17/16.
 */
public class SignUpInfo {
    @SerializedName("username")
    private String username;

    public SignUpInfo(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
