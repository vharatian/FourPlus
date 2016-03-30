package com.anashidgames.gerdoo.core.service.model;

/**
 * Created by psycho on 3/30/16.
 */
public class Category {
    private String title;
    private String iconUrl;
    private String dataUrl;
    private boolean hasSubCategory;

    public Category(String title, String iconUrl, String dataUrl, boolean hasSubCategory) {
        this.title = title;
        this.iconUrl = iconUrl;
        this.dataUrl = dataUrl;
        this.hasSubCategory = hasSubCategory;
    }

    public String getTitle() {
        return title;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public boolean hasSubCategory() {
        return hasSubCategory;
    }
}
