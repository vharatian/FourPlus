package com.anashidgames.gerdoo.core.service.model.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 5/7/16.
 */
public class GetCategoryTopicsParams {
    @SerializedName("categoryId")
    private String categoryId;

    public GetCategoryTopicsParams(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }
}
