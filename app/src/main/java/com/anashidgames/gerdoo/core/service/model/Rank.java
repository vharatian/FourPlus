package com.anashidgames.gerdoo.core.service.model;

import com.anashidgames.gerdoo.core.service.model.server.LeaderBoardItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by psycho on 3/31/16.
 */
public class Rank {

    @SerializedName("rank")
    private int rank;
    @SerializedName("score")
    private int score;
    @SerializedName("name")
    private String name;
    @SerializedName("userId")
    private String userId;

    public Rank(int rank, int score, String name, String userId) {
        this.rank = rank;
        this.score = score;
        this.name = name;
        this.userId = userId;
    }

    public int getRank() {
        return rank;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

}
