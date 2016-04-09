package com.anashidgames.gerdoo.core.service.model;

/**
 * Created by psycho on 3/27/16.
 */
public class HomeItem {
    public static final int TYPE_BANNER_CATEGORY = 1;
    public static final int TYPE_BANNER_URL = 2;
    public static final int TYPE_CATEGORY = 3;

    private int type;
    private String dataUrl;

    private String title;
    private double aspectRatio;
    private String clickUrl;

    public HomeItem(int type, String dataUrl, String title, double aspectRatio, String clickUrl) {
        this.type = type;
        this.dataUrl = dataUrl;
        this.aspectRatio = aspectRatio;
        this.clickUrl = clickUrl;
        this.title = title;
    }

    public HomeItem(int type, String dataUrl, double aspectRatio, String clickUrl) {
        this.type = type;
        this.dataUrl = dataUrl;
        this.aspectRatio = aspectRatio;
        this.clickUrl = clickUrl;
    }

    public HomeItem(int type, String dataUrl, String title) {
        this.type = type;
        this.dataUrl = dataUrl;
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public String getTitle() {
        return title;
    }

    public double getAspectRatio() {
        return aspectRatio;
    }

    public String getClickUrl() {
        return clickUrl;
    }
}
