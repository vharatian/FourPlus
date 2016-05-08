package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 3/27/16.
 */
public class HomeItem {
    public static final int TYPE_BANNER_CATEGORY = 1;
    public static final int TYPE_BANNER_URL = 2;
    public static final int TYPE_CATEGORY = 3;

    @SerializedName("type")
    private int type;


    @SerializedName("title")
    private String title;
    @SerializedName("categoryId")
    private String categoryId;

    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("aspectRatio")
    private double aspectRatio;
    @SerializedName("clickData")
    private String clickData;

    public HomeItem() {
    }

    public HomeItem(String title, String categoryId) {
        this.type = TYPE_CATEGORY;
        this.title = title;
        this.categoryId = categoryId;
    }

    public HomeItem(String imageUrl, double aspectRatio, String clickData) {
        this.imageUrl = imageUrl;
        this.aspectRatio = aspectRatio;
        this.clickData = clickData;
        this.type = TYPE_BANNER_URL;
    }

    public HomeItem(String title, String clickData, double aspectRatio, String imageUrl) {
        this.title = title;
        this.clickData = clickData;
        this.aspectRatio = aspectRatio;
        this.imageUrl = imageUrl;
        type = TYPE_BANNER_CATEGORY;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getAspectRatio() {
        return aspectRatio;
    }

    public String getClickData() {
        return clickData;
    }
}
