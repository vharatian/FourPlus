package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 5/12/16.
 */
public class SignInInfo {
    @SerializedName("userName")
    private String userName;
    @SerializedName("password")
    private String password;

    public SignInInfo(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
