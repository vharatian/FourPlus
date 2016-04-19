package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 4/6/16.
 */
public class SignInParameters {
    @SerializedName("username")
    private String email;
    @SerializedName("password")
    private String password;

    public SignInParameters(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
