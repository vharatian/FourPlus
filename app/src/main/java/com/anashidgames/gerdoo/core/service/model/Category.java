package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 3/30/16.
 */
public class Category {
    @SerializedName("categoryId")
    private String categoryId;
    @SerializedName("title")
    private String title;
    @SerializedName("iconUrl")
    private String iconUrl;
    @SerializedName("hasSubCategory")
    private boolean hasSubCategory = false;
    @SerializedName("color")
    private String color;


    public Category(String categoryId, String title, String iconUrl, boolean hasSubCategory, String color) {
        this.categoryId = categoryId;
        this.title = title;
        this.iconUrl = iconUrl;
        this.hasSubCategory = hasSubCategory;
        this.color = color;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getTitle() {
        return title;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public boolean hasSubCategory() {
        return hasSubCategory;
    }

    public String getColor() {
        return color;
    }
}
