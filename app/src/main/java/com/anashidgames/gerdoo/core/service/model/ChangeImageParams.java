package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 5/19/16.
 */
public class ChangeImageParams {
    @SerializedName("newUrl")
    private String newUrl;

    public ChangeImageParams(String newUrl) {
        this.newUrl = newUrl;
    }

    public String getNewUrl() {
        return newUrl;
    }
}
