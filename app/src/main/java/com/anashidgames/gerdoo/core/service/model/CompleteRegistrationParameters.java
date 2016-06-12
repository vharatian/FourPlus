package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 6/5/16.
 */
public class CompleteRegistrationParameters {
    @SerializedName("userId")
    private String userId;
    @SerializedName("lastPassword")
    private String lastPassword;
    @SerializedName("newUsername")
    private String newUsername;
    @SerializedName("newPassword")
    private String newPassword;
    @SerializedName("email")
    private String email;

    public CompleteRegistrationParameters(String userId, String lastPassword, String email, String newPassword) {
        this.userId = userId;
        this.lastPassword = lastPassword;
        this.newPassword = newPassword;
        this.email = email;
        this.newUsername = email;
    }
}
