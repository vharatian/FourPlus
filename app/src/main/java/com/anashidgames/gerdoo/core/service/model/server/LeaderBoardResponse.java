package com.anashidgames.gerdoo.core.service.model.server;

import com.anashidgames.gerdoo.core.service.model.Rank;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by psycho on 4/24/16.
 */
public class LeaderBoardResponse {

    @SerializedName("ranks")
    private List<Rank> ranks;
    @SerializedName("myRank")
    private int myRank;

    public LeaderBoardResponse(List<Rank> ranks, int myRank) {
        this.ranks = ranks;
        this.myRank = myRank;
    }

    public List<Rank> getRanks() {
        return ranks;
    }

    public int getMyRank() {
        return myRank;
    }
}
