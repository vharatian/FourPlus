package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by psycho on 5/19/16.
 */
public class UploadResponse {

    @SerializedName("savedFilesUrls")
    private List<String> savedFilesUrls;

    public List<String> getSavedFilesUrls() {
        return savedFilesUrls;
    }
}
