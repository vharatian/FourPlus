package com.anashidgames.gerdoo.core.service.model.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 5/7/16.
 */
public class GetSubCategoriesParams {
    @SerializedName("categoryId")
    private String categoryId;

    public GetSubCategoriesParams(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }
}
