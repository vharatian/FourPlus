package com.anashidgames.gerdoo.core.service.model.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 4/17/16.
 */
public class SignUpParameters {

    private String firstName;
    private String lastName;
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("email")
    private String email;
    @SerializedName("phoneNumber")
    private String phoneNumber;

    public SignUpParameters(String email, String phoneNumber, String password) {
        this.username = email;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
