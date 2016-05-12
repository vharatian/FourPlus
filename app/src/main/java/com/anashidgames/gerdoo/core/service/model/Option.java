package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 5/8/16.
 */
public class Option {
    @SerializedName("title")
    private String title;

    public Option(String optionId, String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
