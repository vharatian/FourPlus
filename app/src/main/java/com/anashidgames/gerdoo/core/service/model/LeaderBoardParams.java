package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 5/8/16.
 */
public class LeaderBoardParams {
    @SerializedName("leaderBoardId")
    private String leaderBoardId;
    @SerializedName("page")
    private int page;

    public LeaderBoardParams(String leaderBoardId, int page) {
        this.leaderBoardId = leaderBoardId;
        this.page = page;
    }

    public String getLeaderBoardId() {
        return leaderBoardId;
    }

    public int getPage() {
        return page;
    }
}
