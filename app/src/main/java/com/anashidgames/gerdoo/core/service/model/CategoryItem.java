package com.anashidgames.gerdoo.core.service.model;

/**
 * Created by psycho on 3/29/16.
 */
public class CategoryItem {
    private String imageUrl;
    private String title;
    private String dataUrl;

    public CategoryItem(String imageUrl, String title, String dataUrl) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.dataUrl = dataUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDataUrl() {
        return dataUrl;
    }
}
