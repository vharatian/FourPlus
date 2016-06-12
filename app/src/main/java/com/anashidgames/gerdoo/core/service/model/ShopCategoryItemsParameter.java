package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 6/12/16.
 */
public class ShopCategoryItemsParameter {

    @SerializedName("categoryId")
    private String categoryId;

    public ShopCategoryItemsParameter(String categoryId) {
        this.categoryId = categoryId;
    }
}
