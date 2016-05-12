package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 5/12/16.
 */
public class GustSignUpInfo {
    @SerializedName("username")
    private String userName;
    @SerializedName("password")
    private String password;
    @SerializedName("id")
    private String userId;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }
}
