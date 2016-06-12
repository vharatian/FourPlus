package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 6/12/16.
 */
public class ShopItem {
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("title")
    private String title;
    @SerializedName("subTitle")
    private String subTitle;
    @SerializedName("cafeBazaarKey")
    private String cafeBazaarKey;

    public ShopItem(String imageUrl, String title, String subTitle, String cafebazaarKey) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.subTitle = subTitle;
        this.cafeBazaarKey = cafebazaarKey;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getCafeBazaarKey() {
        return cafeBazaarKey;
    }
}
