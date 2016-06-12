package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by psycho on 5/23/16.
 */
public class ShopCategoryData {
    @SerializedName("rowId")
    private String rowId;
    @SerializedName("title")
    private String title;

    public ShopCategoryData(String rowId, String title) {
        this.rowId = rowId;
        this.title = title;
    }

    public String getCategoryId() {
        return rowId;
    }

    public String getTitle() {
        return title;
    }
}
