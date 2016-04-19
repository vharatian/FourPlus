package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 3/22/16.
 */
public class AuthenticationInfo {
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("expires_in")
    private int expiresIn;
    private long creationTime = System.currentTimeMillis();

    public AuthenticationInfo(String accessToken, String refreshToken, int expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public boolean isValid() {
        return accessToken != null && !accessToken.isEmpty() &&
                refreshToken != null && !refreshToken.isEmpty();
    }

    public boolean expired(){
        return (System.currentTimeMillis() > creationTime + expiresIn);
    }
}
