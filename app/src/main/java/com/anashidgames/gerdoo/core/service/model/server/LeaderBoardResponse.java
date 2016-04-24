package com.anashidgames.gerdoo.core.service.model.server;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by psycho on 4/24/16.
 */
public class LeaderBoardResponse {
    @SerializedName("usersProfile")
    private List<LeaderBoardItem> usersProfile;
    @SerializedName("message")
    private String message;

    public LeaderBoardResponse(List<LeaderBoardItem> usersProfile, String message) {
        this.usersProfile = usersProfile;
        this.message = message;
    }

    public List<LeaderBoardItem> getUsersProfile() {
        return usersProfile;
    }

    public String getMessage() {
        return message;
    }
}
