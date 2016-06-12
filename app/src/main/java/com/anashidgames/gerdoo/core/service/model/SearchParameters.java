package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 6/12/16.
 */
public class SearchParameters {

    @SerializedName("query")
    private String query;

    @SerializedName("receivedCount")
    private final int receivedCount;

    public SearchParameters(String query, int receivedCount) {
        this.query = query;
        this.receivedCount = receivedCount;
    }


}
