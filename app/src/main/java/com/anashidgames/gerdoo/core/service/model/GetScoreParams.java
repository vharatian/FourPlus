package com.anashidgames.gerdoo.core.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psycho on 5/21/16.
 */
public class GetScoreParams {
    @SerializedName("matchMakingName")
    private String matchMakingName;

    public GetScoreParams(String matchMakingName) {
        this.matchMakingName = matchMakingName;
    }

    public String getMatchMakingName() {
        return matchMakingName;
    }
}
