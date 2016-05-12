package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 5/12/16.
 */
public class Score {
    @SerializedName("userId")
    private String userId;
    @SerializedName("score")
    private int score;

    public String getUserId() {
        return userId;
    }

    public int getScore() {
        return score;
    }
}
