package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 6/12/16.
 */
public class EditProfileParameters {
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;

    public EditProfileParameters(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
