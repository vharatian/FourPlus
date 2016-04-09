package com.anashidgames.gerdoo.core.service.model;

/**
 * Created by psycho on 4/6/16.
 */
public class SignInParameters {
    private String email;
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
