package com.anashidgames.gerdoo.view.row;

import android.content.Intent;

/**
 * Created by psycho on 4/17/16.
 */
public class Row {
    private String title;
    private String iconUrl;
    private String color;

    public Row(String title, String iconUrl, String color) {
        this.title = title;
        this.iconUrl = iconUrl;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getTitle() {
        return title;
    }

}
