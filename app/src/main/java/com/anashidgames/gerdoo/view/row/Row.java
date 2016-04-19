package com.anashidgames.gerdoo.view.row;

import android.content.Intent;

/**
 * Created by psycho on 4/17/16.
 */
public class Row {
    private String title;
    private String iconUrl;
    private int color;

    public Row(String title, String iconUrl, int color) {
        this.title = title;
        this.iconUrl = iconUrl;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getTitle() {
        return title;
    }

}
