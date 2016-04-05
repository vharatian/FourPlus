package com.anashidgames.gerdoo.pages.topic.list;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by psycho on 11/13/15.
 */
public class PsychoListResponse<T> {

    @SerializedName("list")
    private List<T> list;
    @SerializedName("next_page")
    private String nextPage = null;

    public PsychoListResponse(List<T> list) {
        this.list = list;
    }

    public PsychoListResponse(List<T> list, String nextPage) {
        this.list = list;
        this.nextPage = nextPage;
    }

    public List<T> getList() {
        return list;
    }

    public String getNextPage() {
        return nextPage;
    }
}
