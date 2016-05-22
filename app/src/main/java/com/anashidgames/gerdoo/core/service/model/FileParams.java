package com.anashidgames.gerdoo.core.service.model;


import com.google.gson.annotations.SerializedName;

import okhttp3.RequestBody;

/**
 * Created by psycho on 5/19/16.
 */
public class FileParams {
    @SerializedName("fileToUpload")
    private RequestBody fileToUpload;
    @SerializedName("path")
    private String path;
    @SerializedName("replacing")
    private boolean replacing;
    @SerializedName("extract")
    private boolean extract;

    public FileParams(RequestBody fileToUpload, String path) {
        this.fileToUpload = fileToUpload;
        this.path = path;
        replacing = false;
        extract = false;
    }

    public RequestBody getFileToUpload() {
        return fileToUpload;
    }

    public String getPath() {
        return path;
    }

    public boolean isReplacing() {
        return replacing;
    }

    public boolean isExtract() {
        return extract;
    }
}
