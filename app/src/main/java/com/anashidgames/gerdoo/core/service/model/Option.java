package com.anashidgames.gerdoo.core.service.model;

/**
 * Created by psycho on 5/8/16.
 */
public class Option {
    private String optionId;
    private String title;

    public Option(String optionId, String title) {
        this.optionId = optionId;
        this.title = title;
    }

    public String getOptionId() {
        return optionId;
    }

    public String getTitle() {
        return title;
    }
}
