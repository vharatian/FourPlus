package com.anashidgames.gerdoo.core.service.model.parameters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 4/23/16.
 */
public class LeaderBoardParams {
    @SerializedName("leaderboardId")
    private String  leaderBoardId;
    @SerializedName("topPlayersCount")
    private int count;

    public LeaderBoardParams() {
        this("571640fce4b0dc73ff7a4278", 10);
    }

    public LeaderBoardParams(String leaderBoardId, int count) {
        this.leaderBoardId = leaderBoardId;
        this.count = count;
    }
}
