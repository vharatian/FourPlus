package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 3/22/16.
 */
public class AuthenticationInfo {
    public static final int ERROR = 30*60;
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("expires_in")
    private int expiresIn;
    @SerializedName("token_type")
    private String tokenType;

    private long creationTime;

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
        return (System.currentTimeMillis() > creationTime + (expiresIn - ERROR)*1000);
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public boolean hasRefreshToken() {
        return refreshToken != null && !refreshToken.isEmpty();
    }

    public void setCreationTime(long time){
        this.creationTime = time;
    }

}
