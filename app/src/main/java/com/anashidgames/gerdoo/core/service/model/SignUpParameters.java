package com.anashidgames.gerdoo.core.service.model;

/**
 * Created by psycho on 4/17/16.
 */
public class SignUpParameters {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;

    public SignUpParameters(String email, String password) {
        this.username = email;
        this.password = password;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
